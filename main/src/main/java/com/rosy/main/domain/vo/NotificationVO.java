package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NotificationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private Byte type;

    private String typeText;

    private Byte isRead;

    private Long orderId;

    private LocalDateTime createTime;
}
