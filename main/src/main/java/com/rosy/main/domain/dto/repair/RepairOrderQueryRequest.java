package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报修单查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 报修单号
     */
    private String orderNo;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消
     */
    private Integer status;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 搜索关键词
     */
    private String searchText;
}
