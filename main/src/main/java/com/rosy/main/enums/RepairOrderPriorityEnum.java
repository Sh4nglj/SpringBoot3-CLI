package com.rosy.main.enums;

import lombok.Getter;

@Getter
public enum RepairOrderPriorityEnum {

    LOW((byte) 1, "低"),
    MEDIUM((byte) 2, "中"),
    HIGH((byte) 3, "高"),
    URGENT((byte) 4, "紧急");

    private final Byte code;
    private final String desc;

    RepairOrderPriorityEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Byte code) {
        for (RepairOrderPriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority.getDesc();
            }
        }
        return "未知优先级";
    }

    public static RepairOrderPriorityEnum getByCode(Byte code) {
        for (RepairOrderPriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return null;
    }
}
