package com.rosy.common.enums;

import lombok.Getter;

/**
 * 通知类型枚举
 */
@Getter
public enum NotificationTypeEnum {

    SYSTEM((byte) 0, "系统通知"),
    ORDER((byte) 1, "工单通知"),
    REPAIR((byte) 2, "维修通知");

    private final Byte value;
    private final String text;

    NotificationTypeEnum(Byte value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(Byte value) {
        if (value == null) {
            return "";
        }
        for (NotificationTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type.text;
            }
        }
        return "";
    }
}
