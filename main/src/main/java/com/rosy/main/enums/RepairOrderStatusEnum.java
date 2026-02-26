package com.rosy.main.enums;

import lombok.Getter;

@Getter
public enum RepairOrderStatusEnum {

    PENDING((byte) 0, "待处理"),
    IN_PROGRESS((byte) 1, "维修中"),
    COMPLETED((byte) 2, "已完成"),
    CANCELLED((byte) 3, "已取消");

    private final Byte code;
    private final String desc;

    RepairOrderStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Byte code) {
        for (RepairOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "未知状态";
    }

    public static RepairOrderStatusEnum getByCode(Byte code) {
        for (RepairOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
