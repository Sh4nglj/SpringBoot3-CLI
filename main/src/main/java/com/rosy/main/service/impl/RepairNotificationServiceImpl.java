package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairNotification;
import com.rosy.main.mapper.RepairNotificationMapper;
import com.rosy.main.service.IRepairNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知表 服务实现类
 *
 * @author Rosy
 */
@Service
public class RepairNotificationServiceImpl extends ServiceImpl<RepairNotificationMapper, RepairNotification> implements IRepairNotificationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendNotification(Long userId, Long orderId, String title, String content, Byte type) {
        RepairNotification notification = new RepairNotification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead((byte) 0);

        return save(notification);
    }

    @Override
    public List<RepairNotification> getUnreadNotifications(Long userId) {
        LambdaQueryWrapper<RepairNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairNotification::getUserId, userId)
                .eq(RepairNotification::getIsRead, 0)
                .orderByDesc(RepairNotification::getCreateTime);

        return list(wrapper);
    }

    @Override
    public List<RepairNotification> getUserNotifications(Long userId) {
        LambdaQueryWrapper<RepairNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairNotification::getUserId, userId)
                .orderByDesc(RepairNotification::getCreateTime);

        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean markAsRead(Long notificationId) {
        LambdaUpdateWrapper<RepairNotification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RepairNotification::getId, notificationId)
                .set(RepairNotification::getIsRead, 1)
                .set(RepairNotification::getReadTime, LocalDateTime.now());

        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean markAllAsRead(Long userId) {
        LambdaUpdateWrapper<RepairNotification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RepairNotification::getUserId, userId)
                .eq(RepairNotification::getIsRead, 0)
                .set(RepairNotification::getIsRead, 1)
                .set(RepairNotification::getReadTime, LocalDateTime.now());

        return update(wrapper);
    }
}
