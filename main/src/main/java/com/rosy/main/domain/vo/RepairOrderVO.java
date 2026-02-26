package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private Integer deviceType;

    private String deviceTypeName;

    private String deviceLocation;

    private Integer faultType;

    private String faultTypeName;

    private String faultDescription;

    private String faultImages;

    private Integer status;

    private String statusName;

    private Integer priority;

    private String priorityName;

    private Integer assignmentMethod;

    private String assignmentMethodName;

    private Long assigneeId;

    private Date assignedTime;

    private Date startedTime;

    private Date completedTime;

    private String repairResult;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
}
