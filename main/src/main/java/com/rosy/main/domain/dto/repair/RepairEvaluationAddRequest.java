package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维修评价添加请求
 */
@Data
public class RepairEvaluationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报修单ID
     */
    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    /**
     * 评分：1-5星
     */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为 1 星")
    @Max(value = 5, message = "评分最大为 5 星")
    private Integer rating;

    /**
     * 评价内容
     */
    @Size(max = 1000, message = "评价内容长度不能超过 1000 个字符")
    private String content;

    /**
     * 服务态度评分
     */
    @Min(value = 1, message = "服务态度评分最小为 1 星")
    @Max(value = 5, message = "服务态度评分最大为 5 星")
    private Integer serviceRating;

    /**
     * 维修质量评分
     */
    @Min(value = 1, message = "维修质量评分最小为 1 星")
    @Max(value = 5, message = "维修质量评分最大为 5 星")
    private Integer qualityRating;

    /**
     * 响应速度评分
     */
    @Min(value = 1, message = "响应速度评分最小为 1 星")
    @Max(value = 5, message = "响应速度评分最大为 5 星")
    private Integer speedRating;
}
