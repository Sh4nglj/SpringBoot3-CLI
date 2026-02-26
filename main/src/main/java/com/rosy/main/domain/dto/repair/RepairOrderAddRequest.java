package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;

    @NotBlank(message = "设备位置不能为空")
    private String deviceLocation;

    @NotNull(message = "故障类型不能为空")
    private Integer faultType;

    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    private String faultImages;

    @Min(value = 0, message = "优先级值必须在0-3之间")
    @Max(value = 3, message = "优先级值必须在0-3之间")
    private Integer priority;
}
