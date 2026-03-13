package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.entity.RepairRating;
import com.rosy.main.domain.vo.repair.RepairRatingVO;

/**
 * 评价服务接口
 */
public interface IRepairRatingService extends IService<RepairRating> {

    /**
     * 提交评价
     */
    Long submitRating(RepairRatingRequest request, Long userId);

    /**
     * 根据工单ID查询评价
     */
    RepairRatingVO getRatingByOrderId(Long orderId);

    /**
     * 查询维修人员的评价列表
     */
    Page<RepairRatingVO> pageRepairerRatings(Long repairerId, int current, int pageSize);

    /**
     * 计算维修人员的平均评分
     */
    Double getAverageRating(Long repairerId);

    /**
     * 转换为VO
     */
    RepairRatingVO convertToVO(RepairRating rating);
}
