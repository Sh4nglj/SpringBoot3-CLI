package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.NotificationStatusEnum;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public void sendRepairCompletedNotification(Long userId, Long repairOrderId, String repairResult) {
        String title = "维修工单已完成";
        String content = "您好，您的报修工单已处理完成。维修结果：" + (repairResult != null ? repairResult : "已修复");
        sendNotification(userId, NotificationTypeEnum.REPAIR_COMPLETED.getValue(), title, content, repairOrderId);
    }

    @Override
    public void sendNotification(Long userId, Integer type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getValue());
        this.save(notification);
    }
}