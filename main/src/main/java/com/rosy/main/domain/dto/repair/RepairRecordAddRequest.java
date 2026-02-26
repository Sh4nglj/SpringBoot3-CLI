package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    @Positive(message = "工单ID必须为正数")
    private Long orderId;

    @NotNull(message = "记录类型不能为空")
    @Min(value = 1, message = "记录类型值范围为1-3")
    @Max(value = 3, message = "记录类型值范围为1-3")
    private Byte recordType;

    @Size(max = 1000, message = "记录内容长度不能超过1000个字符")
    private String recordContent;

    private List<String> recordImages;
}
