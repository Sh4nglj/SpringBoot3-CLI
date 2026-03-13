package com.rosy.main.domain.vo.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录VO
 */
@Data
@Schema(description = "维修记录VO")
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工单ID")
    private Long orderId;

    @Schema(description = "维修人员ID")
    private Long repairerId;

    @Schema(description = "维修人员名称")
    private String repairerName;

    @Schema(description = "操作类型")
    private Byte actionType;

    @Schema(description = "操作类型文本")
    private String actionTypeText;

    @Schema(description = "操作内容")
    private String content;

    @Schema(description = "照片URL列表")
    private List<String> photos;

    @Schema(description = "费用")
    private BigDecimal cost;

    @Schema(description = "更换配件信息")
    private String spareParts;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
