package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    private Long repairOrderId;

    @NotNull(message = "记录类型不能为空")
    private Integer recordType;

    @NotBlank(message = "记录内容不能为空")
    private String content;

    private String images;
}
