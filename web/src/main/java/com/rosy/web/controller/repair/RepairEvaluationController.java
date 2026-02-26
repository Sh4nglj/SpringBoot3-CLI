package com.rosy.web.controller.repair;

import cn.hutool.core.bean.BeanUtil;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/evaluation")
public class RepairEvaluationController {

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addEvaluation(@RequestBody RepairEvaluationAddRequest addRequest) {
        RepairOrder order = repairOrderService.getById(addRequest.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.COMPLETED.getCode().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单未完成，无法评价");
        }
        RepairEvaluation existingEvaluation = repairEvaluationService.getByOrderId(addRequest.getOrderId());
        if (existingEvaluation != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该工单已评价");
        }
        RepairEvaluation evaluation = BeanUtil.copyProperties(addRequest, RepairEvaluation.class);
        evaluation.setUserId(order.getUserId());
        evaluation.setRepairerId(order.getRepairerId());
        boolean result = repairEvaluationService.save(evaluation);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加评价失败");
        return ApiResponse.success(evaluation.getId());
    }

    @GetMapping("/get")
    public ApiResponse getEvaluationByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairEvaluation evaluation = repairEvaluationService.getByOrderId(orderId);
        return ApiResponse.success(repairEvaluationService.getRepairEvaluationVO(evaluation));
    }
}
