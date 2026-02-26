package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private String deviceType;

    private String deviceLocation;

    private String faultType;

    private Byte status;

    private Byte priority;

    private Long repairerId;

    private Long userId;

    private LocalDateTime createTimeStart;

    private LocalDateTime createTimeEnd;
}
