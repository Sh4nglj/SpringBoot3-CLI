package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Byte recordType;

    private String recordTypeText;

    private String recordContent;

    private List<String> recordImages;

    private Long repairerId;

    private String repairerName;

    private LocalDateTime createTime;
}
