package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分配工单请求
 */
@Data
@Schema(description = "分配工单请求")
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true)
    private Long orderId;

    @NotNull(message = "维修人员ID不能为空")
    @Schema(description = "维修人员ID", required = true)
    private Long repairerId;

    @Schema(description = "分配类型：0-自动，1-手动")
    private Byte assignType = 1;
}
