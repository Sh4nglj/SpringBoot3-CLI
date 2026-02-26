package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修记录表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Data
@TableName("repair_record")
public class RepairRecord implements Serializable {

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
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修开始时间
     */
    private LocalDateTime startTime;

    /**
     * 维修结束时间
     */
    private LocalDateTime endTime;

    /**
     * 维修内容
     */
    private String repairContent;

    /**
     * 更换配件
     */
    private String replacedParts;

    /**
     * 维修费用
     */
    private BigDecimal repairCost;

    /**
     * 维修结果：1-修复成功，2-无法修复，3-需要返厂
     */
    private Integer repairResult;

    /**
     * 维修后照片URL
     */
    private String repairImages;

    /**
     * 备注
     */
    private String remark;

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
