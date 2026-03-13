package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 故障照片表
 *
 * @author Rosy
 */
@Data
@TableName("repair_photo")
public class RepairPhoto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 照片URL
     */
    private String photoUrl;

    /**
     * 照片名称
     */
    private String photoName;

    /**
     * 排序
     */
    private Integer sortOrder;

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
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;
}
