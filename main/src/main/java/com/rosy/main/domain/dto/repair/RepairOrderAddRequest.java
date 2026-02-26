package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RepairOrderAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @NotBlank(message = "设备类型不能为空")
    @Size(max = 100, message = "设备类型长度不能超过100个字符")
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    @Size(max = 200, message = "设备位置长度不能超过200个字符")
    private String deviceLocation;

    @NotBlank(message = "故障类型不能为空")
    @Size(max = 100, message = "故障类型长度不能超过100个字符")
    private String faultType;

    @Size(max = 1000, message = "故障描述长度不能超过1000个字符")
    private String faultDescription;

    private List<String> faultImages;

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String userPhone;
}
