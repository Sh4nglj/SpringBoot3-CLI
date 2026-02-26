package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum FaultTypeEnum {

    HARDWARE("硬件故障", 0),
    SOFTWARE("软件故障", 1),
    NETWORK("网络故障", 2),
    POWER("电源故障", 3),
    DAMAGE("损坏", 4),
    ABNORMAL("异常", 5),
    NO_POWER("无法开机", 6),
    NOISE("异响", 7),
    OTHER("其他", 99);

    private final String text;

    private final Integer value;

    FaultTypeEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static FaultTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (FaultTypeEnum anEnum : FaultTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
