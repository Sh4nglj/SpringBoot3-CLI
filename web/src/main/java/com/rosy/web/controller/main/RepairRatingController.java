package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.RepairRatingVO;
import com.rosy.main.service.IRepairRatingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 维修评价 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/rating")
public class RepairRatingController {
    @Resource
    IRepairRatingService repairRatingService;

    /**
     * 根据订单ID获取评价详情
     */
    @GetMapping("/get/order")
    public ApiResponse getRatingByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getOrderId, orderId);
        RepairRating rating = repairRatingService.getOne(wrapper);
        if (rating == null) {
            return ApiResponse.success(null);
        }
        return ApiResponse.success(repairRatingService.getRepairRatingVO(rating));
    }

    /**
     * 根据ID获取评价详情
     */
    @GetMapping("/get")
    public ApiResponse getRatingById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRating rating = repairRatingService.getById(id);
        ThrowUtils.throwIf(rating == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(rating);
    }

    /**
     * 分页获取维修人员的评价列表
     */
    @GetMapping("/worker/list")
    public ApiResponse getWorkerRatings(@RequestParam Long workerId,
                                        @RequestParam(defaultValue = "1") long current,
                                        @RequestParam(defaultValue = "10") long size) {
        if (workerId == null || workerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getWorkerId, workerId)
                .orderByDesc(RepairRating::getCreateTime);
        Page<RepairRating> ratingPage = repairRatingService.page(new Page<>(current, size), wrapper);
        Page<RepairRatingVO> ratingVOPage = PageUtils.convert(ratingPage, repairRatingService::getRepairRatingVO);
        return ApiResponse.success(ratingVOPage);
    }

    /**
     * 获取维修人员的平均评分
     */
    @GetMapping("/worker/average")
    public ApiResponse getWorkerAverageRating(@RequestParam Long workerId) {
        if (workerId == null || workerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Double averageRating = repairRatingService.getWorkerAverageRating(workerId);
        return ApiResponse.success(averageRating);
    }
}
