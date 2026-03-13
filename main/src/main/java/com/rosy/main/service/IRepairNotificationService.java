package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairNotification;

import java.util.List;

/**
 * 通知表 服务类
 *
 * @author Rosy
 */
public interface IRepairNotificationService extends IService<RepairNotification> {

    /**
     * 发送通知
     *
     * @param userId  接收用户ID
     * @param orderId 关联工单ID
     * @param title   通知标题
     * @param content 通知内容
     * @param type    通知类型
     * @return 是否成功
     */
    Boolean sendNotification(Long userId, Long orderId, String title, String content, Byte type);

    /**
     * 获取用户未读通知列表
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    List<RepairNotification> getUnreadNotifications(Long userId);

    /**
     * 获取用户所有通知列表
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    List<RepairNotification> getUserNotifications(Long userId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    Boolean markAsRead(Long notificationId);

    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean markAllAsRead(Long userId);
}
