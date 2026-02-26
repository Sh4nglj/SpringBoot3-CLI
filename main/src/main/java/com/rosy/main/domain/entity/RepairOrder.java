package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Integer deviceType;

    private String deviceLocation;

    private Integer faultType;

    private String faultDescription;

    private String faultImages;

    private Integer status;

    private Integer priority;

    private Integer assignmentMethod;

    private Long assigneeId;

    private LocalDateTime assignedTime;

    private LocalDateTime startedTime;

    private LocalDateTime completedTime;

    private String repairResult;

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
