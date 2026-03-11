package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加维修过程记录请求
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class RepairProcessAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 维修人员ID
     */
    private Long workerId;

    /**
     * 过程描述
     */
    private String description;

    /**
     * 操作类型（1：开始维修 2：更换配件 3：维修完成 4：其他）
     */
    private Integer operationType;

    /**
     * 备注
     */
    private String remark;
}
