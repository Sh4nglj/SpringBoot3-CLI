package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.service.IRepairEvaluationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/evaluation")
public class RepairEvaluationController {

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addEvaluation(@RequestBody RepairEvaluationAddRequest request) {
        Long evaluationId = repairEvaluationService.addEvaluation(request);
        return ApiResponse.success(evaluationId);
    }

    @GetMapping("/get")
    public ApiResponse getEvaluationByOrderId(Long repairOrderId) {
        if (repairOrderId == null || repairOrderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairEvaluation evaluation = repairEvaluationService.getByRepairOrderId(repairOrderId);
        return ApiResponse.success(evaluation);
    }
}
