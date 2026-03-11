package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修记录表
 *
 * @author Rosy
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
    private Long repairerId;

    /**
     * 操作类型：1-接单，2-开始维修，3-维修记录，4-更换配件，5-完成维修
     */
    private Byte actionType;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 照片URL，多个用逗号分隔
     */
    private String photos;

    /**
     * 费用
     */
    private BigDecimal cost;

    /**
     * 更换配件信息（JSON）
     */
    private String spareParts;

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
    private Byte isDeleted;
}
