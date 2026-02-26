package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    @Resource
    @Lazy
    private IRepairOrderService repairOrderService;

    @Override
    public Long addEvaluation(RepairEvaluationAddRequest request) {
        RepairOrder repairOrder = repairOrderService.getById(request.getRepairOrderId());
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.COMPLETED.getValue().equals(repairOrder.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有已完成的工单才能评价");
        }
        RepairEvaluation existing = getByRepairOrderId(request.getRepairOrderId());
        if (existing != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已经评价过了");
        }
        RepairEvaluation evaluation = BeanUtil.copyProperties(request, RepairEvaluation.class);
        this.save(evaluation);
        return evaluation.getId();
    }

    @Override
    public RepairEvaluation getByRepairOrderId(Long repairOrderId) {
        LambdaQueryWrapper<RepairEvaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairEvaluation::getRepairOrderId, repairOrderId);
        return this.getOne(queryWrapper);
    }
}
