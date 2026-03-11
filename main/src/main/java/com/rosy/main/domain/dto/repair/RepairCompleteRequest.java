package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 完成维修请求
 *
 * @author Rosy
 */
@Data
@Schema(description = "完成维修请求")
public class RepairCompleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @Schema(description = "工单ID", required = true)
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 维修结果描述
     */
    @Schema(description = "维修结果描述", required = true)
    @NotNull(message = "维修结果描述不能为空")
    private String result;
}
