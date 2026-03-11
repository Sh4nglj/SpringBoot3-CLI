package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.repair.NotificationVO;

/**
 * 通知服务接口
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 创建通知
     *
     * @param userId   接收用户ID
     * @param senderId 发送者ID
     * @param title    标题
     * @param content  内容
     * @param type     类型
     * @param relatedId 关联ID
     */
    void createNotification(Long userId, Long senderId, String title, String content, Byte type, Long relatedId);

    /**
     * 分页获取用户通知
     *
     * @param userId   用户ID
     * @param current  当前页
     * @param pageSize 每页大小
     * @param type     通知类型（可选）
     * @return 通知列表
     */
    Page<NotificationVO> getUserNotifications(Long userId, int current, int pageSize, Byte type);

    /**
     * 标记通知为已读
     *
     * @param id     通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long id, Long userId);

    /**
     * 标记所有通知为已读
     *
     * @param userId 用户ID
     * @return 已读数量
     */
    int markAllAsRead(Long userId);

    /**
     * 获取未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    long getUnreadCount(Long userId);

    /**
     * 转换为VO
     *
     * @param notification 通知实体
     * @return 通知VO
     */
    NotificationVO convertToVO(Notification notification);
}
