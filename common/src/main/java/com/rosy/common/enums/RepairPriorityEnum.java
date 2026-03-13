package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报修优先级枚举
 */
@Getter
public enum RepairPriorityEnum {

    LOW("低", (byte)1),
    MEDIUM("中", (byte)2),
    HIGH("高", (byte)3),
    URGENT("紧急", (byte)4);

    private final String text;
    private final Byte value;

    RepairPriorityEnum(String text, Byte value) {
        this.text = text;
        this.value = value;
    }

    public static List<Byte> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static RepairPriorityEnum getEnumByValue(Byte value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairPriorityEnum anEnum : RepairPriorityEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getTextByValue(Byte value) {
        RepairPriorityEnum enumByValue = getEnumByValue(value);
        return enumByValue != null ? enumByValue.getText() : "未知";
    }
}
