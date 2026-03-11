package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 维修人员查询请求
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RepairWorkerQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 维修人员姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 负责区域
     */
    private String area;

    /**
     * 状态（1：可用 2：忙碌 3：休息）
     */
    private Integer status;
}
