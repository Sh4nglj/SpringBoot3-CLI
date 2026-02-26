package com.rosy.main.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {

    SITE_MESSAGE((byte) 1, "站内信"),
    SMS((byte) 2, "短信");

    private final Byte code;
    private final String desc;

    NotificationTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
