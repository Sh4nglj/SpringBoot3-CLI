package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正数")
    private Long orderId;

    @NotNull(message = "维修人员ID不能为空")
    @Positive(message = "维修人员ID必须为正数")
    private Long repairerId;

    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级值范围为1-4")
    @Max(value = 4, message = "优先级值范围为1-4")
    private Byte priority;
}
