package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 评价请求
 */
@Data
@Schema(description = "评价请求")
public class RepairRatingRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true)
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于1")
    @Max(value = 5, message = "评分不能大于5")
    @Schema(description = "评分：1-5星", required = true)
    private Byte rating;

    @Length(max = 500, message = "评价内容不能超过500字")
    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价标签")
    private List<String> tags;
}
