package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报修工单表
 *
 * @author Rosy
 */
@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    private String orderNo;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障描述
     */
    private String description;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消
     */
    private Byte status;

    /**
     * 分配的维修人员ID
     */
    private Long assigneeId;

    /**
     * 分配类型：0-自动，1-手动
     */
    private Byte assignType;

    /**
     * 分配时间
     */
    private LocalDateTime assignTime;

    /**
     * 期望完成时间
     */
    private LocalDateTime expectCompleteTime;

    /**
     * 实际完成时间
     */
    private LocalDateTime actualCompleteTime;

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
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;
}
