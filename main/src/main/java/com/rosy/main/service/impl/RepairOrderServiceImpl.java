package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.*;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报修工单表 服务实现类
 *
 * @author Rosy
 */
@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final IRepairPhotoService repairPhotoService;
    private final IRepairProcessService repairProcessService;
    private final IRepairRatingService repairRatingService;
    private final IRepairWorkerService repairWorkerService;
    private final IRepairNotificationService repairNotificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(RepairOrderCreateRequest request) {
        // 生成工单编号
        String orderNo = generateOrderNo();

        // 创建工单
        RepairOrder order = new RepairOrder();
        order.setOrderNo(orderNo);
        order.setUserId(request.getUserId());
        order.setDeviceType(request.getDeviceType());
        order.setLocation(request.getLocation());
        order.setFaultType(request.getFaultType());
        order.setDescription(request.getDescription());
        order.setPriority(request.getPriority() != null ? request.getPriority() : (byte) 2);
        order.setStatus((byte) 0); // 待处理

        save(order);

        // 保存照片
        if (request.getPhotoUrls() != null && !request.getPhotoUrls().isEmpty()) {
            repairPhotoService.savePhotos(order.getId(), request.getPhotoUrls());
        }

        // 自动分配
        autoAssignOrder(order.getId());

        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignOrder(RepairOrderAssignRequest request) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 更新工单信息
        order.setAssigneeId(request.getAssigneeId());
        order.setAssignType(request.getAssignType() != null ? request.getAssignType() : (byte) 1);
        order.setAssignTime(LocalDateTime.now());
        order.setExpectCompleteTime(request.getExpectCompleteTime());
        order.setStatus((byte) 1); // 已分配

        boolean success = updateById(order);

        if (success) {
            // 增加维修人员工单数量
            repairWorkerService.incrementOrderCount(request.getAssigneeId());

            // 添加维修过程记录
            RepairWorker worker = repairWorkerService.getById(request.getAssigneeId());
            String workerName = worker != null ? worker.getName() : "未知";
            repairProcessService.addProcess(order.getId(), request.getAssigneeId(), workerName, (byte) 1, "工单已分配");

            // 发送通知给维修人员
            repairNotificationService.sendNotification(
                    request.getAssigneeId(),
                    order.getId(),
                    "新工单分配",
                    "您有新的维修工单待处理，工单编号：" + order.getOrderNo(),
                    (byte) 2
            );
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean autoAssignOrder(Long orderId) {
        // 获取最空闲的维修人员
        RepairWorker worker = repairWorkerService.getLeastBusyWorker();
        if (worker == null) {
            return false;
        }

        RepairOrderAssignRequest request = new RepairOrderAssignRequest();
        request.setOrderId(orderId);
        request.setAssigneeId(worker.getId());
        request.setAssignType((byte) 0); // 自动分配

        return assignOrder(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        order.setStatus((byte) 2); // 维修中
        boolean success = updateById(order);

        if (success) {
            // 添加维修过程记录
            RepairWorker worker = repairWorkerService.getById(order.getAssigneeId());
            String workerName = worker != null ? worker.getName() : "未知";
            repairProcessService.addProcess(orderId, order.getAssigneeId(), workerName, (byte) 1, "维修人员已接单");

            // 发送通知给用户
            repairNotificationService.sendNotification(
                    order.getUserId(),
                    orderId,
                    "工单已接单",
                    "您的报修工单已被接单，维修人员正在处理中，工单编号：" + order.getOrderNo(),
                    (byte) 2
            );
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startRepair(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        // 添加维修过程记录
        RepairWorker worker = repairWorkerService.getById(order.getAssigneeId());
        String workerName = worker != null ? worker.getName() : "未知";
        repairProcessService.addProcess(orderId, order.getAssigneeId(), workerName, (byte) 2, "开始维修");

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeRepair(RepairCompleteRequest request) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        order.setStatus((byte) 3); // 已完成
        order.setActualCompleteTime(LocalDateTime.now());
        boolean success = updateById(order);

        if (success) {
            // 减少维修人员工单数量
            repairWorkerService.decrementOrderCount(order.getAssigneeId());

            // 添加维修过程记录
            RepairWorker worker = repairWorkerService.getById(order.getAssigneeId());
            String workerName = worker != null ? worker.getName() : "未知";
            repairProcessService.addProcess(order.getId(), order.getAssigneeId(), workerName, (byte) 4, request.getResult());

            // 发送通知给用户
            repairNotificationService.sendNotification(
                    order.getUserId(),
                    order.getId(),
                    "工单已完成",
                    "您的报修工单已完成，请确认并评价，工单编号：" + order.getOrderNo(),
                    (byte) 2
            );
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rateOrder(RepairRatingRequest request) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 3) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单未完成，无法评价");
        }

        // 保存评价
        boolean success = repairRatingService.addRating(request);

        if (success) {
            // 添加维修过程记录
            repairProcessService.addProcess(order.getId(), order.getUserId(), "用户", (byte) 6, "用户已评价：" + request.getRating() + "星");

            // 发送通知给维修人员
            repairNotificationService.sendNotification(
                    order.getAssigneeId(),
                    order.getId(),
                    "工单已评价",
                    "您完成的工单已收到用户评价：" + request.getRating() + "星，工单编号：" + order.getOrderNo(),
                    (byte) 2
            );
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(Long orderId) {
        return cancelOrder(orderId, "用户取消");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(Long orderId, String reason) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() == 3 || order.getStatus() == 4) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单已完成或已取消");
        }

        order.setStatus((byte) 4); // 已取消
        boolean success = updateById(order);

        if (success) {
            // 减少维修人员工单数量
            if (order.getAssigneeId() != null) {
                repairWorkerService.decrementOrderCount(order.getAssigneeId());
            }

            // 添加维修过程记录
            repairProcessService.addProcess(orderId, order.getUserId(), "用户", (byte) 5, reason);
        }

        return success;
    }

    @Override
    public RepairOrderVO getOrderDetail(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            return null;
        }

        RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);

        // 设置描述信息
        setOrderDesc(vo);

        // 获取照片列表
        List<RepairPhoto> photos = repairPhotoService.getPhotosByOrderId(orderId);
        List<RepairPhotoVO> photoVOs = photos.stream()
                .map(photo -> BeanUtil.copyProperties(photo, RepairPhotoVO.class))
                .collect(Collectors.toList());
        vo.setPhotos(photoVOs);

        // 获取维修过程记录
        List<RepairProcess> processes = repairProcessService.getProcessByOrderId(orderId);
        List<RepairProcessVO> processVOs = processes.stream()
                .map(process -> {
                    RepairProcessVO processVO = BeanUtil.copyProperties(process, RepairProcessVO.class);
                    setProcessDesc(processVO);
                    return processVO;
                })
                .collect(Collectors.toList());
        vo.setProcesses(processVOs);

        // 获取评价信息
        RepairRating rating = repairRatingService.getRatingByOrderId(orderId);
        if (rating != null) {
            vo.setRating(BeanUtil.copyProperties(rating, RepairRatingVO.class));
        }

        // 获取维修人员名称
        if (order.getAssigneeId() != null) {
            RepairWorker worker = repairWorkerService.getById(order.getAssigneeId());
            if (worker != null) {
                vo.setAssigneeName(worker.getName());
            }
        }

        return vo;
    }

    @Override
    public List<RepairOrderVO> getUserOrders(Long userId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getUserId, userId)
                .orderByDesc(RepairOrder::getCreateTime);

        List<RepairOrder> orders = list(wrapper);
        return orders.stream()
                .map(order -> {
                    RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
                    setOrderDesc(vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<RepairOrder> getUserOrders(Long userId, long current, long size) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getUserId, userId)
                .orderByDesc(RepairOrder::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public List<RepairOrderVO> getWorkerOrders(Long workerId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getAssigneeId, workerId)
                .orderByDesc(RepairOrder::getCreateTime);

        List<RepairOrder> orders = list(wrapper);
        return orders.stream()
                .map(order -> {
                    RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
                    setOrderDesc(vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<RepairOrder> getWorkerOrders(Long workerId, long current, long size) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getAssigneeId, workerId)
                .orderByDesc(RepairOrder::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public Boolean updateStatus(Long orderId, Byte status) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        order.setStatus(status);
        return updateById(order);
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (request == null) {
            return wrapper;
        }

        // 订单编号
        if (request.getOrderNo() != null && !request.getOrderNo().isEmpty()) {
            wrapper.like(RepairOrder::getOrderNo, request.getOrderNo());
        }

        // 用户ID
        if (request.getUserId() != null) {
            wrapper.eq(RepairOrder::getUserId, request.getUserId());
        }

        // 维修人员ID
        if (request.getWorkerId() != null) {
            wrapper.eq(RepairOrder::getAssigneeId, request.getWorkerId());
        }

        // 设备类型
        if (request.getDeviceType() != null && !request.getDeviceType().isEmpty()) {
            wrapper.eq(RepairOrder::getDeviceType, request.getDeviceType());
        }

        // 故障类型
        if (request.getFaultType() != null && !request.getFaultType().isEmpty()) {
            wrapper.eq(RepairOrder::getFaultType, request.getFaultType());
        }

        // 订单状态
        if (request.getStatus() != null) {
            wrapper.eq(RepairOrder::getStatus, request.getStatus());
        }

        // 优先级
        if (request.getPriority() != null) {
            wrapper.eq(RepairOrder::getPriority, request.getPriority());
        }

        // 位置
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            wrapper.like(RepairOrder::getLocation, request.getLocation());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(RepairOrder::getCreateTime);

        return wrapper;
    }

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder order) {
        if (order == null) {
            return null;
        }
        RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
        setOrderDesc(vo);
        return vo;
    }

    /**
     * 生成工单编号
     */
    private String generateOrderNo() {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        String random = RandomUtil.randomNumbers(4);
        return "WO" + timestamp + random;
    }

    /**
     * 设置工单描述信息
     */
    private void setOrderDesc(RepairOrderVO vo) {
        // 优先级描述
        if (vo.getPriority() != null) {
            switch (vo.getPriority()) {
                case 1:
                    vo.setPriorityDesc("低");
                    break;
                case 2:
                    vo.setPriorityDesc("中");
                    break;
                case 3:
                    vo.setPriorityDesc("高");
                    break;
                case 4:
                    vo.setPriorityDesc("紧急");
                    break;
                default:
                    vo.setPriorityDesc("未知");
            }
        }

        // 状态描述
        if (vo.getStatus() != null) {
            switch (vo.getStatus()) {
                case 0:
                    vo.setStatusDesc("待处理");
                    break;
                case 1:
                    vo.setStatusDesc("已分配");
                    break;
                case 2:
                    vo.setStatusDesc("维修中");
                    break;
                case 3:
                    vo.setStatusDesc("已完成");
                    break;
                case 4:
                    vo.setStatusDesc("已取消");
                    break;
                default:
                    vo.setStatusDesc("未知");
            }
        }

        // 分配类型描述
        if (vo.getAssignType() != null) {
            switch (vo.getAssignType()) {
                case 0:
                    vo.setAssignTypeDesc("自动分配");
                    break;
                case 1:
                    vo.setAssignTypeDesc("手动分配");
                    break;
                default:
                    vo.setAssignTypeDesc("未知");
            }
        }
    }

    /**
     * 设置维修过程描述信息
     */
    private void setProcessDesc(RepairProcessVO vo) {
        if (vo.getActionType() != null) {
            switch (vo.getActionType()) {
                case 1:
                    vo.setActionTypeDesc("接单");
                    break;
                case 2:
                    vo.setActionTypeDesc("开始维修");
                    break;
                case 3:
                    vo.setActionTypeDesc("维修记录");
                    break;
                case 4:
                    vo.setActionTypeDesc("完成维修");
                    break;
                case 5:
                    vo.setActionTypeDesc("取消工单");
                    break;
                case 6:
                    vo.setActionTypeDesc("用户评价");
                    break;
                default:
                    vo.setActionTypeDesc("未知");
            }
        }
    }
}
