package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 维修过程记录VO
 *
 * @author Rosy
 */
@Data
public class RepairProcessVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作类型：1-接单，2-开始维修，3-维修记录，4-完成维修，5-取消工单
     */
    private Byte actionType;

    /**
     * 操作类型描述
     */
    private String actionTypeDesc;

    /**
     * 操作内容/备注
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
