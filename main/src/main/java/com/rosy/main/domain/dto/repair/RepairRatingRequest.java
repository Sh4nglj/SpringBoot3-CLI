package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 评价请求
 *
 * @author Rosy
 */
@Data
@Schema(description = "评价请求")
public class RepairRatingRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @Schema(description = "工单ID", required = true)
    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 评分：1-5星
     */
    @Schema(description = "评分：1-5星", required = true)
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1星")
    @Max(value = 5, message = "评分最大为5星")
    private Byte rating;

    /**
     * 评价内容
     */
    @Schema(description = "评价内容")
    private String content;
}
