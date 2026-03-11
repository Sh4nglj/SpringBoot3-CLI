package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.mapper.RepairRatingMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评价表 服务实现类
 *
 * @author Rosy
 */
@Service
@RequiredArgsConstructor
public class RepairRatingServiceImpl extends ServiceImpl<RepairRatingMapper, RepairRating> implements IRepairRatingService {

    private final IRepairOrderService repairOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addRating(RepairRatingRequest request) {
        // 检查工单是否存在且已完成
        RepairOrder order = repairOrderService.getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() != 3) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单未完成，不能评价");
        }

        // 检查是否已评价
        if (hasRated(request.getOrderId(), request.getUserId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已评价过该工单");
        }

        RepairRating rating = new RepairRating();
        rating.setOrderId(request.getOrderId());
        rating.setUserId(request.getUserId());
        rating.setRating(request.getRating());
        rating.setContent(request.getContent());

        return save(rating);
    }

    @Override
    public RepairRating getRatingByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getOrderId, orderId);

        return getOne(wrapper);
    }

    @Override
    public Boolean hasRated(Long orderId, Long userId) {
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getOrderId, orderId)
                .eq(RepairRating::getUserId, userId);

        return count(wrapper) > 0;
    }
}
