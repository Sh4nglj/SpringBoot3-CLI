package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private Integer deviceType;

    private String deviceLocation;

    private Integer faultType;

    private Integer status;

    private Integer priority;

    private Long createBy;

    private Long assigneeId;
}
