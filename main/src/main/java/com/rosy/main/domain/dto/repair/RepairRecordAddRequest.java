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
 * 添加维修记录请求
 */
@Data
@Schema(description = "添加维修记录请求")
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true)
    private Long orderId;

    @NotNull(message = "操作类型不能为空")
    @Schema(description = "操作类型：1-接单，2-开始维修，3-维修记录，4-更换配件，5-完成维修", required = true)
    private Byte actionType;

    @NotBlank(message = "操作内容不能为空")
    @Schema(description = "操作内容", required = true)
    private String content;

    @Schema(description = "照片URL列表")
    private List<String> photos;

    @Schema(description = "费用")
    private BigDecimal cost;

    @Schema(description = "更换配件信息")
    private String spareParts;
}
