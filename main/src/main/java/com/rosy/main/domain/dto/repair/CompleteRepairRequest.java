package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 完成维修请求
 */
@Data
@Schema(description = "完成维修请求")
public class CompleteRepairRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true)
    private Long orderId;

    @NotBlank(message = "维修结果说明不能为空")
    @Schema(description = "维修结果说明", required = true)
    private String content;

    @Schema(description = "照片URL列表")
    private List<String> photos;

    @Schema(description = "维修费用")
    private BigDecimal cost;

    @Schema(description = "更换配件信息")
    private String spareParts;
}
