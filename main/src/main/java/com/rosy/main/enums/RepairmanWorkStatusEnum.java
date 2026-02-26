package com.rosy.main.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 维修人员工作状态枚举
 */
@Getter
public enum RepairmanWorkStatusEnum {

    OFFLINE(0, "离线"),
    ONLINE_IDLE(1, "在线空闲"),
    BUSY(2, "忙碌");

    private final Integer value;
    private final String text;

    RepairmanWorkStatusEnum(Integer value, String text) {
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
    public static RepairmanWorkStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairmanWorkStatusEnum anEnum : RepairmanWorkStatusEnum.values()) {
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
        RepairmanWorkStatusEnum enumByValue = getEnumByValue(value);
        return enumByValue == null ? "" : enumByValue.getText();
    }
}
