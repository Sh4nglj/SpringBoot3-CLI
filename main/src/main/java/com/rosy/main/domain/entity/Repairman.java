package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 维修人员表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Data
@TableName("repairman")
public class Repairman implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 工号
     */
    private String employeeNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 专业技能
     */
    private String skills;

    /**
     * 工作状态：0-离线，1-在线空闲，2-忙碌
     */
    private Integer workStatus;

    /**
     * 当前工单数
     */
    private Integer currentOrders;

    /**
     * 总完成工单数
     */
    private Integer totalCompletedOrders;

    /**
     * 平均评分
     */
    private Double averageRating;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

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
