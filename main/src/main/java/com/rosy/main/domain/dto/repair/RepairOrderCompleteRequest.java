package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class RepairOrderCompleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正数")
    private Long orderId;

    @Size(max = 1000, message = "维修结果描述长度不能超过1000个字符")
    private String repairResult;

    private List<String> repairImages;

    private BigDecimal repairCost;
}
