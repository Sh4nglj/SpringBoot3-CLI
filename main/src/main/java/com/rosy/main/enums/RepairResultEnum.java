package com.rosy.main.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 维修结果枚举
 */
@Getter
public enum RepairResultEnum {

    SUCCESS(1, "修复成功"),
    FAILED(2, "无法修复"),
    RETURN_FACTORY(3, "需要返厂");

    private final Integer value;
    private final String text;

    RepairResultEnum(Integer value, String text) {
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
    public static RepairResultEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairResultEnum anEnum : RepairResultEnum.values()) {
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
        RepairResultEnum enumByValue = getEnumByValue(value);
        return enumByValue == null ? "" : enumByValue.getText();
    }
}
