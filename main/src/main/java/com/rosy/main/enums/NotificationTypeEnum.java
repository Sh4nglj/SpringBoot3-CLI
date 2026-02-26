package com.rosy.main.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知类型枚举
 */
@Getter
public enum NotificationTypeEnum {

    SMS(1, "短信"),
    EMAIL(2, "邮件"),
    IN_APP(3, "站内信"),
    PUSH(4, "推送");

    private final Integer value;
    private final String text;

    NotificationTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 获取值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     */
    public static NotificationTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (NotificationTypeEnum anEnum : NotificationTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取文本
     */
    public static String getTextByValue(Integer value) {
        NotificationTypeEnum enumByValue = getEnumByValue(value);
        return enumByValue == null ? "" : enumByValue.getText();
    }
}
