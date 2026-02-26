package com.rosy.main.enums;

import lombok.Getter;

@Getter
public enum RecordTypeEnum {

    ACCEPT_CONFIRM((byte) 1, "接单确认"),
    REPAIR_PROCESS((byte) 2, "维修过程"),
    REPAIR_COMPLETE((byte) 3, "维修完成");

    private final Byte code;
    private final String desc;

    RecordTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Byte code) {
        for (RecordTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "未知记录类型";
    }
}
