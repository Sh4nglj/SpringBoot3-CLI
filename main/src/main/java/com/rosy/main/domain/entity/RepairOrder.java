package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String deviceType;

    private String deviceLocation;

    private String faultType;

    private String faultDescription;

    private String faultImages;

    private Byte status;

    private Byte priority;

    private Byte assignType;

    private Long repairerId;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private Long userId;

    private String userPhone;

    private String repairResult;

    private String repairImages;

    private BigDecimal repairCost;

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Version
    private Byte version;

    @TableLogic
    private Byte isDeleted;
}
