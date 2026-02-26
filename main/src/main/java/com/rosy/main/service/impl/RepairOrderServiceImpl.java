package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.enums.AssignTypeEnum;
import com.rosy.main.enums.RepairOrderPriorityEnum;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final IRepairEvaluationService repairEvaluationService;
    private final INotificationService notificationService;

    public RepairOrderServiceImpl(IRepairEvaluationService repairEvaluationService,
                                   INotificationService notificationService) {
        this.repairEvaluationService = repairEvaluationService;
        this.notificationService = notificationService;
    }

    @Override
    public String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
        return "RO" + dateStr + randomStr;
    }

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        if (repairOrder == null) {
            return null;
        }
        RepairOrderVO vo = BeanUtil.copyProperties(repairOrder, RepairOrderVO.class);
        vo.setStatusText(RepairOrderStatusEnum.getDescByCode(repairOrder.getStatus()));
        vo.setPriorityText(RepairOrderPriorityEnum.getDescByCode(repairOrder.getPriority()));
        if (repairOrder.getAssignType() != null) {
            vo.setAssignTypeText(AssignTypeEnum.getDescByCode(repairOrder.getAssignType()));
        }
        if (repairOrder.getFaultImages() != null) {
            vo.setFaultImages(JSON.parseArray(repairOrder.getFaultImages(), String.class));
        }
        RepairEvaluation evaluation = repairEvaluationService.getByOrderId(repairOrder.getId());
        if (evaluation != null) {
            vo.setEvaluation(repairEvaluationService.getRepairEvaluationVO(evaluation));
        }
        return vo;
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), RepairOrder::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getOrderNo(), RepairOrder::getOrderNo);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getDeviceType(), RepairOrder::getDeviceType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getDeviceLocation(), RepairOrder::getDeviceLocation);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getFaultType(), RepairOrder::getFaultType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), RepairOrder::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getPriority(), RepairOrder::getPriority);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRepairerId(), RepairOrder::getRepairerId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), RepairOrder::getUserId);
        if (queryRequest.getCreateTimeStart() != null) {
            queryWrapper.ge(RepairOrder::getCreateTime, queryRequest.getCreateTimeStart());
        }
        if (queryRequest.getCreateTimeEnd() != null) {
            queryWrapper.le(RepairOrder::getCreateTime, queryRequest.getCreateTimeEnd());
        }
        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                RepairOrder::getId);
        return queryWrapper;
    }

    @Override
    public void assignOrder(RepairOrderAssignRequest assignRequest, boolean isAuto) {
        Long orderId = assignRequest.getOrderId();
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许分配");
        }
        LambdaUpdateWrapper<RepairOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RepairOrder::getId, orderId)
                .set(RepairOrder::getRepairerId, assignRequest.getRepairerId())
                .set(RepairOrder::getPriority, assignRequest.getPriority())
                .set(RepairOrder::getAssignType, isAuto ? AssignTypeEnum.AUTO.getCode() : AssignTypeEnum.MANUAL.getCode())
                .set(RepairOrder::getAssignTime, LocalDateTime.now());
        boolean result = this.update(updateWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "分配工单失败");
        }
    }

    @Override
    public void acceptOrder(Long orderId, Long repairerId) {
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!repairerId.equals(order.getRepairerId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权接单");
        }
        if (!RepairOrderStatusEnum.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许接单");
        }
        LambdaUpdateWrapper<RepairOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RepairOrder::getId, orderId)
                .set(RepairOrder::getStatus, RepairOrderStatusEnum.IN_PROGRESS.getCode())
                .set(RepairOrder::getAcceptTime, LocalDateTime.now());
        boolean result = this.update(updateWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接单失败");
        }
    }

    @Override
    public void completeOrder(Long orderId) {
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.IN_PROGRESS.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许完成");
        }
        LambdaUpdateWrapper<RepairOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RepairOrder::getId, orderId)
                .set(RepairOrder::getStatus, RepairOrderStatusEnum.COMPLETED.getCode())
                .set(RepairOrder::getCompleteTime, LocalDateTime.now());
        boolean result = this.update(updateWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "完成工单失败");
        }
        notificationService.sendRepairCompleteNotification(orderId, order.getUserId(), order.getOrderNo());
    }

    @Override
    public void cancelOrder(Long orderId) {
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (RepairOrderStatusEnum.COMPLETED.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已完成的工单不能取消");
        }
        LambdaUpdateWrapper<RepairOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RepairOrder::getId, orderId)
                .set(RepairOrder::getStatus, RepairOrderStatusEnum.CANCELLED.getCode());
        boolean result = this.update(updateWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "取消工单失败");
        }
    }
}
