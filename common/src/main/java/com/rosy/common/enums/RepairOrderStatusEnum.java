package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum RepairOrderStatusEnum {

    PENDING("待处理", 0),
    IN_PROGRESS("维修中", 1),
    COMPLETED("已完成", 2);

    private final String text;

    private final Integer value;

    RepairOrderStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

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
}
