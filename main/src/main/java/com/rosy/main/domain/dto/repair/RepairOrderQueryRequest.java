package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 报修订单查询请求
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 维修人员ID
     */
    private Long workerId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 位置
     */
    private String location;
}
