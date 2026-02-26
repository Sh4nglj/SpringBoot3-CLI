package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum NotificationStatusEnum {
    UNREAD("未读", 0),
    READ("已读", 1);

    private final String text;
    private final Integer value;

    NotificationStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static NotificationStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (NotificationStatusEnum item : NotificationStatusEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}