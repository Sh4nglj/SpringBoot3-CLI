package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.PageUtils;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.repair.RepairRatingVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.mapper.RepairRatingMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IRepairRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 评价服务实现类
 */
@Service
@RequiredArgsConstructor
public class RepairRatingServiceImpl extends ServiceImpl<RepairRatingMapper, RepairRating> implements IRepairRatingService {

    private final RepairOrderMapper repairOrderMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitRating(RepairRatingRequest request, Long userId) {
        // 检查是否已评价
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getOrderId, request.getOrderId());
        Long count = count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该工单已评价");
        }

        RepairOrder order = repairOrderMapper.selectById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 验证工单状态
        if (!RepairOrderStatusEnum.COMPLETED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单未完成，不能评价");
        }

        // 验证用户权限
        if (order.getUserId() == null || !userId.equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是您的工单");
        }

        RepairRating rating = new RepairRating();
        rating.setOrderId(request.getOrderId());
        rating.setUserId(userId);
        rating.setRepairerId(order.getAssigneeId());
        rating.setRating(request.getRating());
        rating.setContent(request.getContent());

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            rating.setTags(String.join(",", request.getTags()));
        }

        save(rating);

        return rating.getId();
    }

    @Override
    public RepairRatingVO getRatingByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getOrderId, orderId);
        RepairRating rating = getOne(wrapper);
        return rating != null ? convertToVO(rating) : null;
    }

    @Override
    public Page<RepairRatingVO> pageRepairerRatings(Long repairerId, int current, int pageSize) {
        Page<RepairRating> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getRepairerId, repairerId)
                .orderByDesc(RepairRating::getCreateTime);

        Page<RepairRating> ratingPage = page(page, wrapper);
        return PageUtils.convert(ratingPage, this::convertToVO);
    }

    @Override
    public Double getAverageRating(Long repairerId) {
        LambdaQueryWrapper<RepairRating> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRating::getRepairerId, repairerId);
        List<RepairRating> ratings = list(wrapper);
        ratings = ratings != null ? ratings : Collections.emptyList();

        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream()
                .mapToInt(r -> r.getRating() != null ? r.getRating().intValue() : 0)
                .sum();

        return sum / ratings.size();
    }

    @Override
    public RepairRatingVO convertToVO(RepairRating rating) {
        if (rating == null) {
            return null;
        }
        RepairRatingVO vo = BeanUtil.copyProperties(rating, RepairRatingVO.class);

        // 处理标签
        if (StrUtil.isNotBlank(rating.getTags())) {
            vo.setTags(Arrays.asList(rating.getTags().split(",")));
        }

        // 用户信息
        User user = userMapper.selectById(rating.getUserId());
        if (user != null) {
            vo.setUserName(user.getUserName());
            vo.setUserAvatar(user.getUserAvatar());
        }

        // 维修人员信息
        User repairer = userMapper.selectById(rating.getRepairerId());
        if (repairer != null) {
            vo.setRepairerName(repairer.getUserName());
        }

        return vo;
    }
}
