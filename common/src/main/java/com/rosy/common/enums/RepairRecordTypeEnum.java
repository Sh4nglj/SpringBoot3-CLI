package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum RepairRecordTypeEnum {

    ACCEPT("接单", 0),
    START("开始维修", 1),
    PAUSE("暂停", 2),
    RESUME("继续", 3),
    COMPLETE("完成", 4),
    NOTE("备注", 5);

    private final String text;

    private final Integer value;

    RepairRecordTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static RepairRecordTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (RepairRecordTypeEnum anEnum : RepairRecordTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
