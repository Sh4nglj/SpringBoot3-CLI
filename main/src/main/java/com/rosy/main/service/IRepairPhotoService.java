package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairPhoto;

import java.util.List;

/**
 * 故障照片表 服务类
 *
 * @author Rosy
 */
public interface IRepairPhotoService extends IService<RepairPhoto> {

    /**
     * 批量保存照片
     *
     * @param orderId   工单ID
     * @param photoUrls 照片URL列表
     * @return 是否成功
     */
    Boolean savePhotos(Long orderId, List<String> photoUrls);

    /**
     * 根据工单ID获取照片列表
     *
     * @param orderId 工单ID
     * @return 照片列表
     */
    List<RepairPhoto> getPhotosByOrderId(Long orderId);

    /**
     * 删除照片
     *
     * @param photoId 照片ID
     * @return 是否成功
     */
    Boolean deletePhoto(Long photoId);
}
