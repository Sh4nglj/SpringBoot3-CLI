package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.entity.RepairRating;

/**
 * 评价表 服务类
 *
 * @author Rosy
 */
public interface IRepairRatingService extends IService<RepairRating> {

    /**
     * 添加评价
     *
     * @param request 评价请求
     * @return 是否成功
     */
    Boolean addRating(RepairRatingRequest request);

    /**
     * 根据工单ID获取评价
     *
     * @param orderId 工单ID
     * @return 评价信息
     */
    RepairRating getRatingByOrderId(Long orderId);

    /**
     * 检查用户是否已评价
     *
     * @param orderId 工单ID
     * @param userId  用户ID
     * @return 是否已评价
     */
    Boolean hasRated(Long orderId, Long userId);
}
