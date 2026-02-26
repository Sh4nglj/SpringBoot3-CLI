package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

    private Date assignedTime;

    private Date startedTime;

    private Date completedTime;

    private String repairResult;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    private Byte version;

    @TableLogic
    private Byte isDeleted;
}
