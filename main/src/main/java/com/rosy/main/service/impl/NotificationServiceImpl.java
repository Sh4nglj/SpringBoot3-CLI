package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.PageUtils;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.repair.NotificationVO;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 通知服务实现类
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(Long userId, Long senderId, String title, String content, Byte type, Long relatedId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接收用户ID不能为空");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "通知标题不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "通知内容不能为空");
        }
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setReadStatus((byte) 0);
        save(notification);
    }

    @Override
    public Page<NotificationVO> getUserNotifications(Long userId, int current, int pageSize, Byte type) {
        Page<Notification> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(type != null, Notification::getType, type)
                .orderByDesc(Notification::getCreateTime);

        Page<Notification> notificationPage = page(page, wrapper);
        return PageUtils.convert(notificationPage, this::convertToVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long id, Long userId) {
        Notification notification = getById(id);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }
        if (notification.getUserId() == null || !userId.equals(notification.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作此通知");
        }
        if (notification.getReadStatus() != null && notification.getReadStatus() == 1) {
            return true;
        }

        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getId, id)
                .set(Notification::getReadStatus, (byte) 1)
                .set(Notification::getReadTime, LocalDateTime.now());
        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markAllAsRead(Long userId) {
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getReadStatus, (byte) 0)
                .set(Notification::getReadStatus, (byte) 1)
                .set(Notification::getReadTime, LocalDateTime.now());
        return baseMapper.update(null, wrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getReadStatus, (byte) 0);
        return count(wrapper);
    }

    @Override
    public NotificationVO convertToVO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationVO vo = BeanUtil.copyProperties(notification, NotificationVO.class);

        // 设置类型文本
        vo.setTypeText(NotificationTypeEnum.getTextByValue(notification.getType()));

        // 设置发送者名称
        if (notification.getSenderId() != null) {
            User sender = userMapper.selectById(notification.getSenderId());
            if (sender != null) {
                vo.setSenderName(sender.getUserName());
            }
        }

        return vo;
    }
}
