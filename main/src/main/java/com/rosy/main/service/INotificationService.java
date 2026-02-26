package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

public interface INotificationService extends IService<Notification> {

    void sendRepairCompletedNotification(Long userId, Long repairOrderId, String repairResult);

    void sendNotification(Long userId, Integer type, String title, String content, Long relatedId);
}