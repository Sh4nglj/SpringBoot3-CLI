package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单VO
 *
 * @author Rosy
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
     * 工单编号
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
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 优先级描述
     */
    private String priorityDesc;

    /**
     * 状态：0-待处理，1-已分配，2-维修中，3-已完成，4-已取消
     */
    private Byte status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 分配的维修人员ID
     */
    private Long assigneeId;

    /**
     * 分配的维修人员名称
     */
    private String assigneeName;

    /**
     * 分配类型：0-自动，1-手动
     */
    private Byte assignType;

    /**
     * 分配类型描述
     */
    private String assignTypeDesc;

    /**
     * 分配时间
     */
    private LocalDateTime assignTime;

    /**
     * 期望完成时间
     */
    private LocalDateTime expectCompleteTime;

    /**
     * 实际完成时间
     */
    private LocalDateTime actualCompleteTime;

    /**
     * 故障照片列表
     */
    private List<RepairPhotoVO> photos;

    /**
     * 维修过程记录
     */
    private List<RepairProcessVO> processes;

    /**
     * 评价信息
     */
    private RepairRatingVO rating;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
