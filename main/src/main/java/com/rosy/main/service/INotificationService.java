package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

public interface INotificationService extends IService<Notification> {

    void sendRepairCompleteNotification(Long orderId, Long userId, String orderNo);

    void sendSmsNotification(Long userId, String content);

    void markAsRead(Long notificationId, Long userId);

    Long getUnreadCount(Long userId);
}
