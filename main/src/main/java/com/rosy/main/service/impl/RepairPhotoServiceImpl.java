package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairPhoto;
import com.rosy.main.mapper.RepairPhotoMapper;
import com.rosy.main.service.IRepairPhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 故障照片表 服务实现类
 *
 * @author Rosy
 */
@Service
public class RepairPhotoServiceImpl extends ServiceImpl<RepairPhotoMapper, RepairPhoto> implements IRepairPhotoService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePhotos(Long orderId, List<String> photoUrls) {
        if (photoUrls == null || photoUrls.isEmpty()) {
            return false;
        }

        List<RepairPhoto> photos = new ArrayList<>();
        for (int i = 0; i < photoUrls.size(); i++) {
            RepairPhoto photo = new RepairPhoto();
            photo.setOrderId(orderId);
            photo.setPhotoUrl(photoUrls.get(i));
            photo.setSortOrder(i);
            photos.add(photo);
        }

        return saveBatch(photos);
    }

    @Override
    public List<RepairPhoto> getPhotosByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairPhoto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairPhoto::getOrderId, orderId)
                .orderByAsc(RepairPhoto::getSortOrder);

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePhoto(Long photoId) {
        return removeById(photoId);
    }
}
