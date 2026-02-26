package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

/**
 * <p>
 * 通知服务接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 发送通知
     *
     * @param userId    接收用户ID
     * @param type      通知类型
     * @param title     标题
     * @param content   内容
     * @param bizType   业务类型
     * @param bizId     业务ID
     * @return 是否成功
     */
    boolean sendNotification(Long userId, Integer type, String title, String content, String bizType, Long bizId);

    /**
     * 发送站内信
     *
     * @param userId    接收用户ID
     * @param title     标题
     * @param content   内容
     * @param bizType   业务类型
     * @param bizId     业务ID
     * @return 是否成功
     */
    boolean sendInAppNotification(Long userId, String title, String content, String bizType, Long bizId);

    /**
     * 发送短信通知
     *
     * @param userId    接收用户ID
     * @param phone     手机号
     * @param content   内容
     * @param bizType   业务类型
     * @param bizId     业务ID
     * @return 是否成功
     */
    boolean sendSmsNotification(Long userId, String phone, String content, String bizType, Long bizId);

    /**
     * 发送推送通知
     *
     * @param userId    接收用户ID
     * @param title     标题
     * @param content   内容
     * @param bizType   业务类型
     * @param bizId     业务ID
     * @return 是否成功
     */
    boolean sendPushNotification(Long userId, String title, String content, String bizType, Long bizId);

    /**
     * 批量发送通知
     *
     * @param userIds   接收用户ID列表
     * @param type      通知类型
     * @param title     标题
     * @param content   内容
     * @param bizType   业务类型
     * @param bizId     业务ID
     * @return 是否成功
     */
    boolean batchSendNotification(List<Long> userIds, Integer type, String title, String content, String bizType, Long bizId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long notificationId);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 发送维修完成通知
     *
     * @param userId      用户ID
     * @param orderNo     报修单号
     * @param deviceType  设备类型
     * @param repairmanName 维修人员姓名
     * @return 是否成功
     */
    boolean sendRepairCompleteNotification(Long userId, String orderNo, String deviceType, String repairmanName);
}
