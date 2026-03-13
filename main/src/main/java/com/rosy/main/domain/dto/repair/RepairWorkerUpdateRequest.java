package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新维修人员请求
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class RepairWorkerUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

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
     * 专业技能
     */
    private String skills;

    /**
     * 状态（1：可用 2：忙碌 3：休息）
     */
    private Integer status;
}
