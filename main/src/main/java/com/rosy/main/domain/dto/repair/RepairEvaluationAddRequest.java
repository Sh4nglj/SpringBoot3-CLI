package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairEvaluationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正数")
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分值范围为1-5")
    @Max(value = 5, message = "评分值范围为1-5")
    private Byte rating;

    @Size(max = 500, message = "评价内容长度不能超过500个字符")
    private String content;
}
