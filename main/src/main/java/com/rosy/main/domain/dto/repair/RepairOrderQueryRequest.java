package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报修单查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报修单查询请求")
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "报修单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "故障类型")
    private String faultType;

    @Schema(description = "状态")
    private Byte status;

    @Schema(description = "优先级")
    private Byte priority;

    @Schema(description = "维修人员ID")
    private Long assigneeId;
}
