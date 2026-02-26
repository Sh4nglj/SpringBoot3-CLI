package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {
    SYSTEM("系统通知", 0),
    REPAIR_COMPLETED("维修完成通知", 1),
    REPAIR_ASSIGNED("工单分配通知", 2),
    OTHER("其他通知", 99);

    private final String text;
    private final Integer value;

    NotificationTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static NotificationTypeEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (NotificationTypeEnum item : NotificationTypeEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}