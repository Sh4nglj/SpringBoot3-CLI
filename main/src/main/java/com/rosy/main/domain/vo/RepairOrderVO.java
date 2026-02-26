package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private String deviceType;

    private String deviceLocation;

    private String faultType;

    private String faultDescription;

    private List<String> faultImages;

    private Byte status;

    private String statusText;

    private Byte priority;

    private String priorityText;

    private Byte assignType;

    private String assignTypeText;

    private Long repairerId;

    private String repairerName;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private Long userId;

    private String userName;

    private String userPhone;

    private String repairResult;

    private List<String> repairImages;

    private BigDecimal repairCost;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private RepairEvaluationVO evaluation;
}
