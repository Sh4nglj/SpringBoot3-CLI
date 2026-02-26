package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum DeviceTypeEnum {

    COMPUTER("电脑", 0),
    PRINTER("打印机", 1),
    AIR_CONDITIONER("空调", 2),
    PROJECTOR("投影仪", 3),
    ELEVATOR("电梯", 4),
    FURNITURE("家具", 5),
    LIGHTING("照明设备", 6),
    NETWORK("网络设备", 7),
    OTHER("其他", 99);

    private final String text;

    private final Integer value;

    DeviceTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static DeviceTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (DeviceTypeEnum anEnum : DeviceTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
