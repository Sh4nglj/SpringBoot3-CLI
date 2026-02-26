package com.rosy.main.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报修单状态枚举
 */
@Getter
public enum RepairOrderStatusEnum {

    PENDING(0, "待处理"),
    ASSIGNED(1, "已分配"),
    REPAIRING(2, "维修中"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final Integer value;
    private final String text;

    RepairOrderStatusEnum(Integer value, String text) {
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
    public static RepairOrderStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairOrderStatusEnum anEnum : RepairOrderStatusEnum.values()) {
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
        RepairOrderStatusEnum enumByValue = getEnumByValue(value);
        return enumByValue == null ? "" : enumByValue.getText();
    }
}
