package com.rosy.main.domain.vo.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修单VO
 */
@Data
@Schema(description = "报修单VO")
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "报修单号")
    private String orderNo;

    @Schema(description = "报修用户ID")
    private Long userId;

    @Schema(description = "报修用户名称")
    private String userName;

    @Schema(description = "报修用户电话")
    private String userPhone;

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "设备位置")
    private String deviceLocation;

    @Schema(description = "故障类型")
    private String faultType;

    @Schema(description = "故障描述")
    private String description;

    @Schema(description = "故障照片URL列表")
    private List<String> photos;

    @Schema(description = "优先级")
    private Byte priority;

    @Schema(description = "优先级文本")
    private String priorityText;

    @Schema(description = "状态")
    private Byte status;

    @Schema(description = "状态文本")
    private String statusText;

    @Schema(description = "维修人员ID")
    private Long assigneeId;

    @Schema(description = "维修人员名称")
    private String assigneeName;

    @Schema(description = "分配类型")
    private Byte assignType;

    @Schema(description = "分配类型文本")
    private String assignTypeText;

    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Schema(description = "接单时间")
    private LocalDateTime acceptTime;

    @Schema(description = "完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "期望完成时间")
    private LocalDateTime expectedTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
