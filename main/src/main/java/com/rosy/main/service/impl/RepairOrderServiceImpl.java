package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.Repairman;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.enums.AssignTypeEnum;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.enums.RepairPriorityEnum;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairmanService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报修单表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Slf4j
@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Resource
    private IRepairmanService repairmanService;

    @Resource
    private INotificationService notificationService;

    @Override
    public void validRepairOrder(RepairOrder repairOrder, boolean add) {
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String deviceType = repairOrder.getDeviceType();
        String location = repairOrder.getLocation();
        String faultType = repairOrder.getFaultType();
        String description = repairOrder.getDescription();

        // 创建时，参数不能为空
        if (add) {
            if (StrUtil.hasBlank(deviceType, location, faultType, description)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备类型、位置、故障类型和描述不能为空");
            }
        }

        // 有参数则校验
        if (StrUtil.isNotBlank(deviceType) && deviceType.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备类型过长");
        }
        if (StrUtil.isNotBlank(location) && location.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "设备位置过长");
        }
        if (StrUtil.isNotBlank(faultType) && faultType.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "故障类型过长");
        }
        if (StrUtil.isNotBlank(description) && description.length() > 1000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "故障描述过长");
        }
    }

    @Override
    public QueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest repairOrderQueryRequest) {
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        if (repairOrderQueryRequest == null) {
            return queryWrapper;
        }

        Long id = repairOrderQueryRequest.getId();
        String orderNo = repairOrderQueryRequest.getOrderNo();
        Long userId = repairOrderQueryRequest.getUserId();
        String deviceType = repairOrderQueryRequest.getDeviceType();
        String faultType = repairOrderQueryRequest.getFaultType();
        Integer status = repairOrderQueryRequest.getStatus();
        Integer priority = repairOrderQueryRequest.getPriority();
        Long repairmanId = repairOrderQueryRequest.getRepairmanId();
        String searchText = repairOrderQueryRequest.getSearchText();
        String sortField = repairOrderQueryRequest.getSortField();
        String sortOrder = repairOrderQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(orderNo), "order_no", orderNo);
        queryWrapper.eq(userId != null && userId > 0, "user_id", userId);
        queryWrapper.eq(StrUtil.isNotBlank(deviceType), "device_type", deviceType);
        queryWrapper.eq(StrUtil.isNotBlank(faultType), "fault_type", faultType);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(priority != null, "priority", priority);
        queryWrapper.eq(repairmanId != null && repairmanId > 0, "repairman_id", repairmanId);

        // 搜索文本
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("device_type", searchText)
                    .or()
                    .like("location", searchText)
                    .or()
                    .like("fault_type", searchText)
                    .or()
                    .like("description", searchText));
        }

        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        if (repairOrder == null) {
            return null;
        }
        RepairOrderVO repairOrderVO = new RepairOrderVO();
        BeanUtils.copyProperties(repairOrder, repairOrderVO);

        // 设置状态文本
        repairOrderVO.setStatusText(RepairOrderStatusEnum.getTextByValue(repairOrder.getStatus()));
        repairOrderVO.setPriorityText(RepairPriorityEnum.getTextByValue(repairOrder.getPriority()));
        repairOrderVO.setAssignTypeText(AssignTypeEnum.getTextByValue(repairOrder.getAssignType()));

        // 处理图片列表
        if (StrUtil.isNotBlank(repairOrder.getFaultImages())) {
            repairOrderVO.setFaultImages(List.of(repairOrder.getFaultImages().split(",")));
        }

        return repairOrderVO;
    }

    @Override
    public Page<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> repairOrderPage) {
        List<RepairOrder> repairOrderList = repairOrderPage.getRecords();
        Page<RepairOrderVO> repairOrderVOPage = new Page<>(repairOrderPage.getCurrent(), repairOrderPage.getSize(), repairOrderPage.getTotal());
        if (repairOrderList == null || repairOrderList.isEmpty()) {
            return repairOrderVOPage;
        }

        List<RepairOrderVO> repairOrderVOList = repairOrderList.stream()
                .map(this::getRepairOrderVO)
                .collect(Collectors.toList());
        repairOrderVOPage.setRecords(repairOrderVOList);
        return repairOrderVOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRepairOrder(RepairOrderAddRequest repairOrderAddRequest, HttpServletRequest request) {
        RepairOrder repairOrder = new RepairOrder();
        BeanUtils.copyProperties(repairOrderAddRequest, repairOrder);

        // 生成报修单号
        String orderNo = generateOrderNo();
        repairOrder.setOrderNo(orderNo);

        // 设置默认值
        repairOrder.setStatus(RepairOrderStatusEnum.PENDING.getValue());
        if (repairOrder.getPriority() == null) {
            repairOrder.setPriority(RepairPriorityEnum.MEDIUM.getValue());
        }

        // 处理图片
        if (repairOrderAddRequest.getFaultImages() != null && !repairOrderAddRequest.getFaultImages().isEmpty()) {
            repairOrder.setFaultImages(String.join(",", repairOrderAddRequest.getFaultImages()));
        }

        // 获取当前登录用户ID
        // TODO: 从request中获取当前登录用户ID
        repairOrder.setUserId(1L);

        validRepairOrder(repairOrder, true);
        boolean result = this.save(repairOrder);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建报修单失败");
        }

        return repairOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRepairOrder(RepairOrderUpdateRequest repairOrderUpdateRequest, HttpServletRequest request) {
        Long id = repairOrderUpdateRequest.getId();
        RepairOrder oldRepairOrder = this.getById(id);
        if (oldRepairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 只有待处理状态的报修单可以修改
        if (!oldRepairOrder.getStatus().equals(RepairOrderStatusEnum.PENDING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有待处理状态的报修单可以修改");
        }

        RepairOrder repairOrder = new RepairOrder();
        BeanUtils.copyProperties(repairOrderUpdateRequest, repairOrder);

        // 处理图片
        if (repairOrderUpdateRequest.getFaultImages() != null && !repairOrderUpdateRequest.getFaultImages().isEmpty()) {
            repairOrder.setFaultImages(String.join(",", repairOrderUpdateRequest.getFaultImages()));
        }

        validRepairOrder(repairOrder, false);
        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignRepairOrder(RepairOrderAssignRequest repairOrderAssignRequest, HttpServletRequest request) {
        Long orderId = repairOrderAssignRequest.getOrderId();
        Long repairmanId = repairOrderAssignRequest.getRepairmanId();

        RepairOrder repairOrder = this.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.PENDING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单已被处理");
        }

        // 检查维修人员是否存在且可用
        Repairman repairman = repairmanService.getById(repairmanId);
        if (repairman == null || repairman.getStatus() != 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "维修人员不存在或不可用");
        }

        // 更新报修单
        repairOrder.setRepairmanId(repairmanId);
        repairOrder.setAssignType(repairOrderAssignRequest.getAssignType());
        repairOrder.setAssignTime(LocalDateTime.now());
        repairOrder.setStatus(RepairOrderStatusEnum.ASSIGNED.getValue());

        // 增加维修人员当前工单数
        repairmanService.incrementCurrentOrders(repairmanId);

        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean autoAssignRepairOrder(Long orderId) {
        RepairOrder repairOrder = this.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.PENDING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单已被处理");
        }

        // 自动分配维修人员
        Repairman repairman = repairmanService.autoAssignRepairman(repairOrder.getDeviceType());
        if (repairman == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "暂无可用维修人员");
        }

        // 更新报修单
        repairOrder.setRepairmanId(repairman.getId());
        repairOrder.setAssignType(AssignTypeEnum.AUTO.getValue());
        repairOrder.setAssignTime(LocalDateTime.now());
        repairOrder.setStatus(RepairOrderStatusEnum.ASSIGNED.getValue());

        // 增加维修人员当前工单数
        repairmanService.incrementCurrentOrders(repairman.getId());

        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptRepairOrder(RepairOrderAcceptRequest repairOrderAcceptRequest, HttpServletRequest request) {
        Long orderId = repairOrderAcceptRequest.getOrderId();
        Boolean accept = repairOrderAcceptRequest.getAccept();

        RepairOrder repairOrder = this.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.ASSIGNED.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单状态不正确");
        }

        // TODO: 从request中获取当前登录的维修人员ID，验证是否为分配的维修人员
        Long currentRepairmanId = 1L;
        if (!repairOrder.getRepairmanId().equals(currentRepairmanId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您不是该报修单的分配维修人员");
        }

        if (accept) {
            // 接单
            repairOrder.setStatus(RepairOrderStatusEnum.REPAIRING.getValue());
            repairOrder.setAcceptTime(LocalDateTime.now());
        } else {
            // 拒单，重新分配
            repairOrder.setStatus(RepairOrderStatusEnum.PENDING.getValue());
            repairOrder.setRepairmanId(null);
            repairOrder.setAssignType(null);
            repairOrder.setAssignTime(null);

            // 减少维修人员当前工单数
            repairmanService.decrementCurrentOrders(currentRepairmanId);
        }

        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeRepairOrder(Long orderId, HttpServletRequest request) {
        RepairOrder repairOrder = this.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.REPAIRING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单状态不正确");
        }

        // TODO: 从request中获取当前登录的维修人员ID，验证是否为分配的维修人员
        Long currentRepairmanId = 1L;
        if (!repairOrder.getRepairmanId().equals(currentRepairmanId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您不是该报修单的分配维修人员");
        }

        // 完成报修单
        repairOrder.setStatus(RepairOrderStatusEnum.COMPLETED.getValue());
        repairOrder.setCompleteTime(LocalDateTime.now());

        // 减少维修人员当前工单数，增加总完成工单数
        repairmanService.decrementCurrentOrders(currentRepairmanId);

        // 获取维修人员信息
        Repairman repairman = repairmanService.getById(currentRepairmanId);
        String repairmanName = repairman != null ? repairman.getName() : "";

        // 发送维修完成通知给用户
        boolean notifyResult = notificationService.sendRepairCompleteNotification(
                repairOrder.getUserId(),
                repairOrder.getOrderNo(),
                repairOrder.getDeviceType(),
                repairmanName
        );

        if (!notifyResult) {
            log.warn("维修完成通知发送失败，订单号: {}", repairOrder.getOrderNo());
        }

        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelRepairOrder(Long orderId, HttpServletRequest request) {
        RepairOrder repairOrder = this.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 只有待处理或已分配状态的报修单可以取消
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.PENDING.getValue())
                && !repairOrder.getStatus().equals(RepairOrderStatusEnum.ASSIGNED.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单状态不允许取消");
        }

        // TODO: 从request中获取当前登录用户ID，验证是否为报修用户

        // 如果已分配，减少维修人员当前工单数
        if (repairOrder.getRepairmanId() != null) {
            repairmanService.decrementCurrentOrders(repairOrder.getRepairmanId());
        }

        repairOrder.setStatus(RepairOrderStatusEnum.CANCELLED.getValue());
        return this.updateById(repairOrder);
    }

    /**
     * 生成报修单号
     */
    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%04d", (int) (Math.random() * 10000));
        return "R" + dateStr + randomStr;
    }
}
