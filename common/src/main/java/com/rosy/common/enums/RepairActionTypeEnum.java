package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 维修操作类型枚举
 */
@Getter
public enum RepairActionTypeEnum {

    ACCEPT_ORDER("接单", (byte)1),
    START_REPAIR("开始维修", (byte)2),
    REPAIR_RECORD("维修记录", (byte)3),
    REPLACE_PART("更换配件", (byte)4),
    COMPLETE_REPAIR("完成维修", (byte)5);

    private final String text;
    private final Byte value;

    RepairActionTypeEnum(String text, Byte value) {
        this.text = text;
        this.value = value;
    }

    public static List<Byte> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static RepairActionTypeEnum getEnumByValue(Byte value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairActionTypeEnum anEnum : RepairActionTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getTextByValue(Byte value) {
        RepairActionTypeEnum enumByValue = getEnumByValue(value);
        return enumByValue != null ? enumByValue.getText() : "未知";
    }
}
