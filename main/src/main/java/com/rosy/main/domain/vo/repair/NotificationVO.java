package com.rosy.main.domain.vo.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知VO
 */
@Data
@Schema(description = "通知VO")
public class NotificationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "发送者ID")
    private Long senderId;

    @Schema(description = "发送者名称")
    private String senderName;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "通知类型：0-系统通知，1-工单通知，2-维修通知")
    private Byte type;

    @Schema(description = "类型文本")
    private String typeText;

    @Schema(description = "关联ID")
    private Long relatedId;

    @Schema(description = "读取状态：0-未读，1-已读")
    private Byte readStatus;

    @Schema(description = "读取时间")
    private LocalDateTime readTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
