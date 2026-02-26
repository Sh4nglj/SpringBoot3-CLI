package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

public interface INotificationService extends IService<Notification> {

    void sendAssignNotification(Long orderId, Long repairerId, String orderNo, String deviceType);

    void sendAcceptNotification(Long orderId, Long userId, String orderNo, String repairerName);

    void sendRepairCompleteNotification(Long orderId, Long userId, String orderNo);

    void sendSmsNotification(Long userId, String content);

    void markAsRead(Long notificationId, Long userId);

    Long getUnreadCount(Long userId);
}
