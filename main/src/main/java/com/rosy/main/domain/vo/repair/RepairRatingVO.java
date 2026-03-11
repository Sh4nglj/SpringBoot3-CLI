package com.rosy.main.domain.vo.repair;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价VO
 */
@Data
@Schema(description = "评价VO")
public class RepairRatingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "工单ID")
    private Long orderId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "维修人员ID")
    private Long repairerId;

    @Schema(description = "维修人员名称")
    private String repairerName;

    @Schema(description = "评分：1-5星")
    private Byte rating;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价标签列表")
    private List<String> tags;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
