package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SecurityUtils;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.vo.repair.RepairRatingVO;
import com.rosy.main.service.IRepairRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/repair/rating")
@Tag(name = "评价管理", description = "评价相关接口")
public class RepairRatingController {

    @Resource
    private IRepairRatingService repairRatingService;

    /**
     * 提交评价
     */
    @PostMapping("/submit")
    @ValidateRequest
    @Operation(summary = "提交评价")
    public ApiResponse submitRating(@RequestBody RepairRatingRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        Long ratingId = repairRatingService.submitRating(request, userId);
        return ApiResponse.success(ratingId);
    }

    /**
     * 根据工单ID查询评价
     */
    @GetMapping("/get/order")
    @Operation(summary = "根据工单ID查询评价")
    public ApiResponse getRatingByOrderId(@RequestParam Long orderId) {
        if (orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRatingVO ratingVO = repairRatingService.getRatingByOrderId(orderId);
        return ApiResponse.success(ratingVO);
    }

    /**
     * 查询维修人员的评价列表
     */
    @GetMapping("/list/repairer")
    @Operation(summary = "查询维修人员的评价列表")
    public ApiResponse listRepairerRatings(@RequestParam Long repairerId,
                                           @RequestParam(defaultValue = "1") int current,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        if (repairerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<RepairRatingVO> page = repairRatingService.pageRepairerRatings(repairerId, current, pageSize);
        return ApiResponse.success(page);
    }

    /**
     * 获取维修人员的平均评分
     */
    @GetMapping("/average")
    @Operation(summary = "获取维修人员的平均评分")
    public ApiResponse getAverageRating(@RequestParam Long repairerId) {
        if (repairerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Double averageRating = repairRatingService.getAverageRating(repairerId);
        return ApiResponse.success(averageRating);
    }
}
