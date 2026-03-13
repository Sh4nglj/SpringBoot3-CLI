package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 故障照片VO
 *
 * @author Rosy
 */
@Data
public class RepairPhotoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 照片URL
     */
    private String photoUrl;

    /**
     * 照片名称
     */
    private String photoName;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
