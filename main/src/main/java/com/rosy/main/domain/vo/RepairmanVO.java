package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 维修人员视图对象
 */
@Data
public class RepairmanVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 工号
     */
    private String employeeNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 专业技能
     */
    private String skills;

    /**
     * 工作状态
     */
    private Integer workStatus;

    /**
     * 工作状态文本
     */
    private String workStatusText;

    /**
     * 当前工单数
     */
    private Integer currentOrders;

    /**
     * 总完成工单数
     */
    private Integer totalCompletedOrders;

    /**
     * 平均评分
     */
    private Double averageRating;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
