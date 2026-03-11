package com.rosy.main.domain.dto.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建报修工单请求
 *
 * @author Rosy
 */
@Data
@Schema(description = "创建报修工单请求")
public class RepairOrderCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报修用户ID
     */
    @Schema(description = "报修用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 设备类型
     */
    @Schema(description = "设备类型", required = true)
    @NotBlank(message = "设备类型不能为空")
    private String deviceType;

    /**
     * 设备位置
     */
    @Schema(description = "设备位置", required = true)
    @NotBlank(message = "设备位置不能为空")
    private String location;

    /**
     * 故障类型
     */
    @Schema(description = "故障类型", required = true)
    @NotBlank(message = "故障类型不能为空")
    private String faultType;

    /**
     * 故障描述
     */
    @Schema(description = "故障描述", required = true)
    @NotBlank(message = "故障描述不能为空")
    private String description;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    @Schema(description = "优先级：1-低，2-中，3-高，4-紧急")
    private Byte priority;

    /**
     * 故障照片URL列表
     */
    @Schema(description = "故障照片URL列表")
    private List<String> photoUrls;
}
