package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录视图对象
 */
@Data
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 报修单ID
     */
    private Long orderId;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员姓名
     */
    private String repairmanName;

    /**
     * 维修开始时间
     */
    private LocalDateTime startTime;

    /**
     * 维修结束时间
     */
    private LocalDateTime endTime;

    /**
     * 维修内容
     */
    private String repairContent;

    /**
     * 更换配件
     */
    private String replacedParts;

    /**
     * 维修费用
     */
    private BigDecimal repairCost;

    /**
     * 维修结果
     */
    private Integer repairResult;

    /**
     * 维修结果文本
     */
    private String repairResultText;

    /**
     * 维修后照片URL列表
     */
    private List<String> repairImages;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
