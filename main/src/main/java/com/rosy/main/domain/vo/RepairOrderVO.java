package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修单视图对象
 */
@Data
public class RepairOrderVO implements Serializable {

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
     * 报修用户信息
     */
    private String userName;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备位置
     */
    private String location;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障描述
     */
    private String description;

    /**
     * 故障照片URL列表
     */
    private List<String> faultImages;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 优先级文本
     */
    private String priorityText;

    /**
     * 分配类型
     */
    private Integer assignType;

    /**
     * 分配类型文本
     */
    private String assignTypeText;

    /**
     * 维修人员ID
     */
    private Long repairmanId;

    /**
     * 维修人员姓名
     */
    private String repairmanName;

    /**
     * 分配时间
     */
    private LocalDateTime assignTime;

    /**
     * 接单时间
     */
    private LocalDateTime acceptTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
