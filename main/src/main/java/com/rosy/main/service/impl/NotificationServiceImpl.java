package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 通知服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendNotification(Long userId, Integer type, String title, String content, String bizType, Long bizId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setBizType(bizType);
        notification.setBizId(bizId);
        notification.setIsRead(0);
        notification.setSendStatus(1); // 默认成功，实际应根据发送结果设置

        // 根据通知类型调用不同的发送方式
        boolean sendSuccess = false;
        try {
            NotificationTypeEnum typeEnum = NotificationTypeEnum.getEnumByValue(type);
            if (typeEnum == null) {
                log.error("未知的通知类型: {}", type);
                return false;
            }

            switch (typeEnum) {
                case SMS:
                    sendSuccess = doSendSms(userId, content);
                    break;
                case EMAIL:
                    sendSuccess = doSendEmail(userId, title, content);
                    break;
                case IN_APP:
                    sendSuccess = true; // 站内信直接保存到数据库即可
                    break;
                case PUSH:
                    sendSuccess = doSendPush(userId, title, content);
                    break;
                default:
                    log.error("未实现的通知类型: {}", type);
                    return false;
            }

            notification.setSendStatus(sendSuccess ? 1 : 0);
            if (!sendSuccess) {
                notification.setFailReason("发送失败");
            }
        } catch (Exception e) {
            log.error("发送通知失败", e);
            notification.setSendStatus(0);
            notification.setFailReason(e.getMessage());
        }

        return this.save(notification);
    }

    @Override
    public boolean sendInAppNotification(Long userId, String title, String content, String bizType, Long bizId) {
        return sendNotification(userId, NotificationTypeEnum.IN_APP.getValue(), title, content, bizType, bizId);
    }

    @Override
    public boolean sendSmsNotification(Long userId, String phone, String content, String bizType, Long bizId) {
        // TODO: 集成短信服务（如阿里云短信、腾讯云短信等）
        log.info("发送短信给用户[{}]，手机号[{}]，内容: {}", userId, phone, content);
        // 这里调用实际的短信发送接口
        return true;
    }

    @Override
    public boolean sendPushNotification(Long userId, String title, String content, String bizType, Long bizId) {
        // TODO: 集成推送服务（如极光推送、Firebase Cloud Messaging等）
        log.info("发送推送给用户[{}]，标题[{}]，内容: {}", userId, title, content);
        // 这里调用实际的推送发送接口
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSendNotification(List<Long> userIds, Integer type, String title, String content, String bizType, Long bizId) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        for (Long userId : userIds) {
            sendNotification(userId, type, title, content, bizType, bizId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long notificationId) {
        Notification notification = this.getById(notificationId);
        if (notification == null) {
            return false;
        }

        notification.setIsRead(1);
        notification.setReadTime(LocalDateTime.now());
        return this.updateById(notification);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        queryWrapper.eq("is_deleted", 0);
        return this.count(queryWrapper);
    }

    @Override
    public boolean sendRepairCompleteNotification(Long userId, String orderNo, String deviceType, String repairmanName) {
        String title = "维修完成通知";
        String content = String.format("您的%s报修单[%s]已完成维修，维修人员：%s。请登录系统查看详情并进行评价。",
                deviceType, orderNo, repairmanName);

        // 发送站内信
        boolean inAppResult = sendInAppNotification(userId, title, content, "REPAIR_ORDER", Long.valueOf(orderNo.replace("R", "")));

        // TODO: 根据用户配置决定是否发送短信和推送
        // sendSmsNotification(userId, phone, content, "REPAIR_ORDER", orderId);
        // sendPushNotification(userId, title, content, "REPAIR_ORDER", orderId);

        return inAppResult;
    }

    /**
     * 实际发送短信（需要集成短信服务商）
     */
    private boolean doSendSms(Long userId, String content) {
        // TODO: 集成短信服务
        log.info("发送短信给用户[{}]，内容: {}", userId, content);
        return true;
    }

    /**
     * 实际发送邮件（需要集成邮件服务）
     */
    private boolean doSendEmail(Long userId, String title, String content) {
        // TODO: 集成邮件服务
        log.info("发送邮件给用户[{}]，标题[{}]，内容: {}", userId, title, content);
        return true;
    }

    /**
     * 实际发送推送（需要集成推送服务）
     */
    private boolean doSendPush(Long userId, String title, String content) {
        // TODO: 集成推送服务
        log.info("发送推送给用户[{}]，标题[{}]，内容: {}", userId, title, content);
        return true;
    }
}
