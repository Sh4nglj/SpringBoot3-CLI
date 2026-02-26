package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报修单分配请求
 */
@Data
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报修单ID
     */
    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    /**
     * 维修人员ID
     */
    @NotNull(message = "维修人员ID不能为空")
    private Long repairmanId;

    /**
     * 分配类型：1-自动分配，2-手动分配
     */
    @NotNull(message = "分配类型不能为空")
    @Min(value = 1, message = "分配类型值只能为 1 或 2")
    @Max(value = 2, message = "分配类型值只能为 1 或 2")
    private Integer assignType;
}
