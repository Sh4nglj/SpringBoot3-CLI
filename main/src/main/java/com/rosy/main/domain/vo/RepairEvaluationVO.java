package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 维修评价视图对象
 */
@Data
public class RepairEvaluationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 报修单ID
     */
    private Long orderId;

    /**
     * 评价用户ID
     */
    private Long userId;

    /**
     * 评价用户姓名
     */
    private String userName;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员姓名
     */
    private String repairmanName;

    /**
     * 评分：1-5星
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 服务态度评分
     */
    private Integer serviceRating;

    /**
     * 维修质量评分
     */
    private Integer qualityRating;

    /**
     * 响应速度评分
     */
    private Integer speedRating;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
