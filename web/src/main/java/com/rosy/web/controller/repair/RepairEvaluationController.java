package com.rosy.web.controller.repair;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.service.IRepairEvaluationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 维修评价 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/repair/evaluation")
public class RepairEvaluationController {

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    /**
     * 用户提交评价
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairEvaluation(@RequestBody RepairEvaluationAddRequest repairEvaluationAddRequest,
                                           HttpServletRequest request) {
        Long evaluationId = repairEvaluationService.addRepairEvaluation(repairEvaluationAddRequest, request);
        return ApiResponse.success(evaluationId);
    }

    /**
     * 根据报修单ID获取评价
     */
    @GetMapping("/get-by-order")
    public ApiResponse getRepairEvaluationByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairEvaluationVO repairEvaluationVO = repairEvaluationService.getRepairEvaluationByOrderId(orderId);
        return ApiResponse.success(repairEvaluationVO);
    }

    /**
     * 根据ID获取评价详情
     */
    @GetMapping("/get")
    public ApiResponse getRepairEvaluationById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairEvaluationVO repairEvaluationVO = repairEvaluationService.getRepairEvaluationVO(repairEvaluationService.getById(id));
        return ApiResponse.success(repairEvaluationVO);
    }
}
