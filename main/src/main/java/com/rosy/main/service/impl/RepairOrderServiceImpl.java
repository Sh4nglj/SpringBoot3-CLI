package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.*;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.common.utils.SecurityUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.repair.RepairOrderVO;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 报修单服务实现类
 */
@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final UserMapper userMapper;
    private final RepairRecordMapper repairRecordMapper;
    private final RepairOrderMapper repairOrderMapper;
    private final INotificationService notificationService;
    private final IRepairRecordService repairRecordService;

    private static final AtomicInteger ORDER_COUNTER = new AtomicInteger(0);
    private static final String ORDER_PREFIX = "RO";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitRepairOrder(RepairOrderSubmitRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();

        RepairOrder order = new RepairOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setDeviceType(request.getDeviceType());
        order.setDeviceLocation(request.getDeviceLocation());
        order.setFaultType(request.getFaultType());
        order.setDescription(request.getDescription());
        order.setPriority(request.getPriority() != null ? request.getPriority() : (byte) 2);
        order.setStatus(RepairOrderStatusEnum.PENDING.getValue());

        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            order.setPhotos(String.join(",", request.getPhotos()));
        }

        save(order);

        // 尝试自动分配
        boolean assigned = autoAssignOrder(order.getId());

        // 发送通知给用户确认工单提交
        String title = "工单提交成功";
        String content;
        if (assigned) {
            content = String.format("您的工单【%s】已提交成功并已分配维修人员，我们将尽快为您处理。", order.getOrderNo());
        } else {
            content = String.format("您的工单【%s】已提交成功，正在等待维修人员接单。", order.getOrderNo());
        }
        notificationService.createNotification(userId, null, title, content, NotificationTypeEnum.ORDER.getValue(), order.getId());

        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean autoAssignOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        if (!RepairOrderStatusEnum.PENDING.getValue().equals(order.getStatus())) {
            return false;
        }

        // 根据设备类型查找合适的维修人员
        List<Long> repairerIds = repairOrderMapper.findRepairerIdsByDeviceType(order.getDeviceType());
        repairerIds = repairerIds != null ? repairerIds : Collections.emptyList();

        // 如果没有找到对应技能的维修人员，查找所有维修人员
        if (repairerIds.isEmpty()) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUserRole, "repair")
                    .eq(User::getIsDeleted, 0);
            List<User> users = userMapper.selectList(wrapper);
            repairerIds = users != null ? users.stream().map(User::getId).toList() : Collections.emptyList();
        }

        if (repairerIds.isEmpty()) {
            return false;
        }

        // 选择工单最少的维修人员
        Long bestRepairerId = null;
        int minWorkload = Integer.MAX_VALUE;

        for (Long repairerId : repairerIds) {
            Integer count = repairOrderMapper.countPendingByRepairerId(repairerId);
            if (count == null) count = 0;
            if (count < minWorkload) {
                minWorkload = count;
                bestRepairerId = repairerId;
            }
        }

        if (bestRepairerId == null) {
            return false;
        }

        // 分配工单
        order.setAssigneeId(bestRepairerId);
        order.setAssignType(AssignTypeEnum.AUTO.getValue());
        order.setAssignTime(LocalDateTime.now());
        order.setStatus(RepairOrderStatusEnum.ASSIGNED.getValue());
        boolean result = updateById(order);

        // 创建维修记录
        RepairRecord record = new RepairRecord();
        record.setOrderId(orderId);
        record.setRepairerId(bestRepairerId);
        record.setActionType(RepairActionTypeEnum.ACCEPT_ORDER.getValue());
        record.setContent("系统自动分配工单");
        repairRecordMapper.insert(record);

        // 发送通知给维修人员
        String repairerTitle = "新工单分配通知";
        String repairerContent = String.format("您有新的工单【%s】需要处理，请及时查看。", order.getOrderNo());
        notificationService.createNotification(bestRepairerId, null, repairerTitle, repairerContent, NotificationTypeEnum.ORDER.getValue(), orderId);

        // 发送通知给用户
        String userTitle = "工单已分配";
        String userContent = String.format("您的工单【%s】已分配维修人员，我们将尽快为您处理。", order.getOrderNo());
        notificationService.createNotification(order.getUserId(), null, userTitle, userContent, NotificationTypeEnum.ORDER.getValue(), orderId);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean manualAssignOrder(RepairOrderAssignRequest request) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 检查维修人员是否存在且角色正确
        User repairer = userMapper.selectById(request.getRepairerId());
        if (repairer == null || !"repair".equals(repairer.getUserRole())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的维修人员");
        }

        order.setAssigneeId(request.getRepairerId());
        order.setAssignType(AssignTypeEnum.MANUAL.getValue());
        order.setAssignTime(LocalDateTime.now());
        order.setStatus(RepairOrderStatusEnum.ASSIGNED.getValue());
        boolean result = updateById(order);

        // 发送通知给维修人员
        String repairerTitle = "新工单分配通知";
        String repairerContent = String.format("您有新的工单【%s】需要处理，请及时查看。", order.getOrderNo());
        notificationService.createNotification(request.getRepairerId(), null, repairerTitle, repairerContent, NotificationTypeEnum.ORDER.getValue(), request.getOrderId());

        // 发送通知给用户
        String userTitle = "工单已分配";
        String userContent = String.format("您的工单【%s】已分配维修人员，我们将尽快为您处理。", order.getOrderNo());
        notificationService.createNotification(order.getUserId(), null, userTitle, userContent, NotificationTypeEnum.ORDER.getValue(), request.getOrderId());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptOrder(Long orderId, Long repairerId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        if (!RepairOrderStatusEnum.ASSIGNED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        if (!repairerId.equals(order.getAssigneeId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是分配给您的工单");
        }

        order.setStatus(RepairOrderStatusEnum.IN_PROGRESS.getValue());
        order.setAcceptTime(LocalDateTime.now());
        boolean result = updateById(order);

        if (result) {
            RepairRecord record = new RepairRecord();
            record.setOrderId(orderId);
            record.setRepairerId(repairerId);
            record.setActionType(RepairActionTypeEnum.ACCEPT_ORDER.getValue());
            record.setContent("维修人员已接单，开始维修");
            repairRecordMapper.insert(record);

            // 发送通知给用户
            String title = "维修人员已接单";
            String content = String.format("您的工单【%s】维修人员已接单，正在为您维修。", order.getOrderNo());
            notificationService.createNotification(order.getUserId(), repairerId, title, content, NotificationTypeEnum.ORDER.getValue(), orderId);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeRepair(CompleteRepairRequest request, Long repairerId) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        if (!RepairOrderStatusEnum.IN_PROGRESS.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        if (!repairerId.equals(order.getAssigneeId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是您负责的工单");
        }

        order.setStatus(RepairOrderStatusEnum.PENDING_CONFIRM.getValue());
        order.setCompleteTime(LocalDateTime.now());
        boolean result = updateById(order);

        // 创建完成维修的记录
        RepairRecord record = new RepairRecord();
        record.setOrderId(request.getOrderId());
        record.setRepairerId(repairerId);
        record.setActionType(RepairActionTypeEnum.COMPLETE_REPAIR.getValue());
        record.setContent(request.getContent());
        record.setCost(request.getCost());
        record.setSpareParts(request.getSpareParts());

        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            record.setPhotos(String.join(",", request.getPhotos()));
        }

        repairRecordMapper.insert(record);

        // 发送通知给用户，告知维修已完成，等待确认
        String title = "维修已完成，请确认";
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(String.format("您的工单【%s】维修已完成，请确认维修结果。", order.getOrderNo()));
        if (request.getCost() != null && request.getCost().compareTo(java.math.BigDecimal.ZERO) > 0) {
            contentBuilder.append("\n维修费用：").append(request.getCost()).append("元");
        }
        notificationService.createNotification(order.getUserId(), repairerId, title, contentBuilder.toString(), NotificationTypeEnum.ORDER.getValue(), request.getOrderId());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmComplete(Long orderId, Long userId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        if (!RepairOrderStatusEnum.PENDING_CONFIRM.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        if (!userId.equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是您的工单");
        }

        order.setStatus(RepairOrderStatusEnum.COMPLETED.getValue());
        boolean result = updateById(order);

        // 获取维修记录，整理维修结果信息
        List<RepairRecordVO> records = repairRecordService.getRecordsByOrderId(orderId);
        StringBuilder repairResult = new StringBuilder();
        
        // 统计维修相关信息
        records.stream()
                .filter(r -> RepairActionTypeEnum.COMPLETE_REPAIR.getValue().equals(r.getActionType()))
                .findFirst()
                .ifPresent(r -> {
                    if (StrUtil.isNotBlank(r.getContent())) {
                        repairResult.append("维修说明：").append(r.getContent());
                    }
                    if (r.getCost() != null && r.getCost().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        repairResult.append("\n维修费用：").append(r.getCost()).append("元");
                    }
                });

        // 发送通知给用户
        String title = "维修工单已完成";
        String content;
        if (repairResult.length() > 0) {
            content = String.format("您的工单【%s】已确认完成！\n%s\n感谢使用我们的服务！", order.getOrderNo(), repairResult);
        } else {
            content = String.format("您的工单【%s】已确认完成，感谢使用我们的服务！", order.getOrderNo());
        }
        notificationService.createNotification(userId, null, title, content, NotificationTypeEnum.ORDER.getValue(), orderId);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, Long userId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        if (!userId.equals(order.getUserId()) && !SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        if (RepairOrderStatusEnum.COMPLETED.getValue().equals(order.getStatus()) ||
                RepairOrderStatusEnum.CANCELLED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单无法取消");
        }

        order.setStatus(RepairOrderStatusEnum.CANCELLED.getValue());
        boolean result = updateById(order);

        // 发送通知给用户
        String userTitle = "工单已取消";
        String userContent = String.format("您的工单【%s】已取消。", order.getOrderNo());
        notificationService.createNotification(order.getUserId(), null, userTitle, userContent, NotificationTypeEnum.ORDER.getValue(), orderId);

        // 如果工单已分配，发送通知给维修人员
        if (order.getAssigneeId() != null) {
            String repairerTitle = "工单已取消";
            String repairerContent = String.format("工单【%s】已被用户取消。", order.getOrderNo());
            notificationService.createNotification(order.getAssigneeId(), null, repairerTitle, repairerContent, NotificationTypeEnum.ORDER.getValue(), orderId);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPriority(Long orderId, Byte priority) {
        if (priority < 1 || priority > 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "优先级值无效");
        }

        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        order.setPriority(priority);
        return updateById(order);
    }

    @Override
    public RepairOrderVO getOrderDetail(Long id) {
        RepairOrder order = getById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return convertToVO(order);
    }

    @Override
    public Page<RepairOrderVO> pageOrder(RepairOrderQueryRequest request) {
        Page<RepairOrder> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = buildQueryWrapper(request);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return PageUtils.convert(orderPage, this::convertToVO);
    }

    @Override
    public Page<RepairOrderVO> pageUserOrder(Long userId, RepairOrderQueryRequest request) {
        Page<RepairOrder> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = buildQueryWrapper(request);
        wrapper.eq(RepairOrder::getUserId, userId);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return PageUtils.convert(orderPage, this::convertToVO);
    }

    @Override
    public Page<RepairOrderVO> pageRepairerOrder(Long repairerId, RepairOrderQueryRequest request) {
        Page<RepairOrder> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = buildQueryWrapper(request);
        wrapper.eq(RepairOrder::getAssigneeId, repairerId);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return PageUtils.convert(orderPage, this::convertToVO);
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> buildQueryWrapper(RepairOrderQueryRequest request) {
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();

        if (request != null) {
            QueryWrapperUtil.addCondition(queryWrapper, request.getOrderNo(), RepairOrder::getOrderNo);
            QueryWrapperUtil.addCondition(queryWrapper, request.getUserId(), RepairOrder::getUserId);
            QueryWrapperUtil.addLikeCondition(queryWrapper, request.getDeviceType(), RepairOrder::getDeviceType);
            QueryWrapperUtil.addLikeCondition(queryWrapper, request.getFaultType(), RepairOrder::getFaultType);
            QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), RepairOrder::getStatus);
            QueryWrapperUtil.addCondition(queryWrapper, request.getPriority(), RepairOrder::getPriority);
            QueryWrapperUtil.addCondition(queryWrapper, request.getAssigneeId(), RepairOrder::getAssigneeId);
        }

        queryWrapper.orderByDesc(RepairOrder::getPriority)
                .orderByDesc(RepairOrder::getCreateTime);

        return queryWrapper;
    }

    @Override
    public String generateOrderNo() {
        String dateStr = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        int seq = ORDER_COUNTER.incrementAndGet() % 1000;
        return ORDER_PREFIX + dateStr + String.format("%03d", seq);
    }

    private RepairOrderVO convertToVO(RepairOrder order) {
        if (order == null) {
            return null;
        }
        RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);

        // 处理照片
        if (StrUtil.isNotBlank(order.getPhotos())) {
            vo.setPhotos(Arrays.asList(order.getPhotos().split(",")));
        }

        // 状态文本
        vo.setStatusText(RepairOrderStatusEnum.getTextByValue(order.getStatus()));

        // 优先级文本
        vo.setPriorityText(RepairPriorityEnum.getTextByValue(order.getPriority()));

        // 分配类型文本
        if (order.getAssignType() != null) {
            AssignTypeEnum assignType = AssignTypeEnum.getEnumByValue(order.getAssignType());
            if (assignType != null) {
                vo.setAssignTypeText(assignType.getText());
            }
        }

        // 用户信息
        if (order.getUserId() != null) {
            User user = userMapper.selectById(order.getUserId());
            if (user != null) {
                vo.setUserName(user.getUserName());
                vo.setUserPhone(user.getPhone());
            }
        }

        // 维修人员信息
        if (order.getAssigneeId() != null) {
            User repairer = userMapper.selectById(order.getAssigneeId());
            if (repairer != null) {
                vo.setAssigneeName(repairer.getUserName());
            }
        }

        return vo;
    }
}
