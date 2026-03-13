package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 提交报修申请请求
 */
@Data
@Schema(description = "提交报修申请请求")
public class RepairOrderSubmitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备类型不能为空")
    @Schema(description = "设备类型", required = true)
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    @Schema(description = "设备位置", required = true)
    private String deviceLocation;

    @NotBlank(message = "故障类型不能为空")
    @Schema(description = "故障类型", required = true)
    private String faultType;

    @NotBlank(message = "故障描述不能为空")
    @Length(max = 1000, message = "故障描述不能超过1000字")
    @Schema(description = "故障描述", required = true)
    private String description;

    @Schema(description = "故障照片URL列表")
    private List<String> photos;

    @Schema(description = "优先级：1-低，2-中，3-高，4-紧急")
    private Byte priority;
}
