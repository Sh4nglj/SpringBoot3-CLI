package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 分配类型枚举
 */
@Getter
public enum AssignTypeEnum {

    AUTO("自动分配", (byte)0),
    MANUAL("手动分配", (byte)1);

    private final String text;
    private final Byte value;

    AssignTypeEnum(String text, Byte value) {
        this.text = text;
        this.value = value;
    }

    public static AssignTypeEnum getEnumByValue(Byte value) {
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
}
