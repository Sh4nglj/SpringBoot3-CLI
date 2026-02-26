package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录添加请求
 */
@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报修单ID
     */
    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    /**
     * 维修开始时间
     */
    @NotNull(message = "维修开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 维修结束时间
     */
    @NotNull(message = "维修结束时间不能为空")
    private LocalDateTime endTime;

    /**
     * 维修内容
     */
    @NotBlank(message = "维修内容不能为空")
    @Size(max = 2000, message = "维修内容长度不能超过 2000 个字符")
    private String repairContent;

    /**
     * 更换配件
     */
    @Size(max = 1000, message = "更换配件长度不能超过 1000 个字符")
    private String replacedParts;

    /**
     * 维修费用
     */
    @DecimalMin(value = "0.00", message = "维修费用不能小于 0")
    @DecimalMax(value = "999999.99", message = "维修费用不能超过 999999.99")
    private BigDecimal repairCost;

    /**
     * 维修结果：1-修复成功，2-无法修复，3-需要返厂
     */
    @NotNull(message = "维修结果不能为空")
    @Min(value = 1, message = "维修结果值只能为 1、2 或 3")
    @Max(value = 3, message = "维修结果值只能为 1、2 或 3")
    private Integer repairResult;

    /**
     * 维修后照片URL列表
     */
    private List<String> repairImages;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注长度不能超过 1000 个字符")
    private String remark;
}
