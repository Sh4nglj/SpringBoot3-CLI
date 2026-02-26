package com.rosy.main.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分配类型枚举
 */
@Getter
public enum AssignTypeEnum {

    AUTO(1, "自动分配"),
    MANUAL(2, "手动分配");

    private final Integer value;
    private final String text;

    AssignTypeEnum(Integer value, String text) {
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
    public static AssignTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (AssignTypeEnum anEnum : AssignTypeEnum.values()) {
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
        AssignTypeEnum enumByValue = getEnumByValue(value);
        return enumByValue == null ? "" : enumByValue.getText();
    }
}
