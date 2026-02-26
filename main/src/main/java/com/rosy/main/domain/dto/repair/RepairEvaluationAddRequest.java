package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairEvaluationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    private Long repairOrderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分必须在1-5星之间")
    @Max(value = 5, message = "评分必须在1-5星之间")
    private Integer rating;

    private String content;
}
