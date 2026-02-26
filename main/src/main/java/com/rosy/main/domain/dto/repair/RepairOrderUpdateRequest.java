package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 报修单更新请求
 */
@Data
public class RepairOrderUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 设备类型
     */
    @Size(max = 50, message = "设备类型长度不能超过 50 个字符")
    private String deviceType;

    /**
     * 设备位置
     */
    @Size(max = 200, message = "设备位置长度不能超过 200 个字符")
    private String location;

    /**
     * 故障类型
     */
    @Size(max = 50, message = "故障类型长度不能超过 50 个字符")
    private String faultType;

    /**
     * 故障描述
     */
    @Size(max = 1000, message = "故障描述长度不能超过 1000 个字符")
    private String description;

    /**
     * 故障照片URL列表
     */
    private List<String> faultImages;

    /**
     * 优先级
     */
    @Min(value = 1, message = "优先级值不能小于 1")
    @Max(value = 4, message = "优先级值不能大于 4")
    private Integer priority;
}
