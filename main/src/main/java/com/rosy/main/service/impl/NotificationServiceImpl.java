package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public void sendRepairCompleteNotification(Long orderId, Long userId, String orderNo) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setTitle("维修完成通知");
        notification.setContent("您的报修工单[" + orderNo + "]已完成维修，请确认并评价。");
        notification.setType(NotificationTypeEnum.SITE_MESSAGE.getCode());
        notification.setIsRead((byte) 0);
        boolean result = this.save(notification);
        if (!result) {
            log.error("发送维修完成通知失败，orderId: {}, userId: {}", orderId, userId);
        }
        log.info("发送维修完成通知成功，orderId: {}, userId: {}", orderId, userId);
    }

    @Override
    public void sendSmsNotification(Long userId, String content) {
        log.info("模拟发送短信通知，userId: {}, content: {}", userId, content);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = this.getById(notificationId);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }
        if (!userId.equals(notification.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权操作此通知");
        }
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getId, notificationId)
                .set(Notification::getIsRead, 1);
        this.update(updateWrapper);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0);
        return this.count(queryWrapper);
    }
}
