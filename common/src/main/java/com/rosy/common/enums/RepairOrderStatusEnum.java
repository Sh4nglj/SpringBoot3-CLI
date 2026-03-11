package com.rosy.common.enums;

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

    PENDING("待处理", (byte)0),
    ASSIGNED("已分配", (byte)1),
    IN_PROGRESS("维修中", (byte)2),
    PENDING_CONFIRM("待确认", (byte)3),
    COMPLETED("已完成", (byte)4),
    CANCELLED("已取消", (byte)5);

    private final String text;
    private final Byte value;

    RepairOrderStatusEnum(String text, Byte value) {
        this.text = text;
        this.value = value;
    }

    public static List<Byte> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static RepairOrderStatusEnum getEnumByValue(Byte value) {
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

    public static String getTextByValue(Byte value) {
        RepairOrderStatusEnum enumByValue = getEnumByValue(value);
        return enumByValue != null ? enumByValue.getText() : "未知";
    }
}
