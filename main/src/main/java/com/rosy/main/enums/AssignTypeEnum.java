package com.rosy.main.enums;

import lombok.Getter;

@Getter
public enum AssignTypeEnum {

    AUTO((byte) 1, "自动分配"),
    MANUAL((byte) 2, "手动分配");

    private final Byte code;
    private final String desc;

    AssignTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Byte code) {
        for (AssignTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "未知分配方式";
    }
}
