package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairEvaluationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Long userId;

    private String userName;

    private Long repairerId;

    private String repairerName;

    private Byte rating;

    private String content;

    private LocalDateTime createTime;
}
