package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维修人员接单请求
 */
@Data
public class RepairOrderAcceptRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报修单ID
     */
    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    /**
     * 是否接单：true-接单，false-拒单
     */
    @NotNull(message = "接单状态不能为空")
    private Boolean accept;

    /**
     * 拒单原因
     */
    @Size(max = 500, message = "拒单原因长度不能超过 500 个字符")
    private String rejectReason;
}
