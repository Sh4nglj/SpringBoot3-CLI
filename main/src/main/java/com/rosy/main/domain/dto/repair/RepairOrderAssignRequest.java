package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分配工单请求
 *
 * @author Rosy
 */
@Data
@Schema(description = "分配工单请求")
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @Schema(description = "工单ID", required = true)
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 维修人员ID
     */
    @Schema(description = "维修人员ID", required = true)
    @NotNull(message = "维修人员ID不能为空")
    private Long assigneeId;

    /**
     * 分配类型：0-自动，1-手动
     */
    @Schema(description = "分配类型：0-自动，1-手动")
    private Byte assignType;

    /**
     * 期望完成时间
     */
    @Schema(description = "期望完成时间")
    private LocalDateTime expectCompleteTime;
}
