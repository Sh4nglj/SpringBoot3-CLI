package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修评价表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Data
@TableName("repair_evaluation")
public class RepairEvaluation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 维修人员ID
     */
    private Long repairmanId;

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
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;
}
