package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "站内信通知")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    @Operation(summary = "获取我的通知列表")
    @GetMapping("/my")
    public ApiResponse getMyNotifications(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        Page<Notification> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        if (status != null) {
            wrapper.eq(Notification::getStatus, status);
        }
        wrapper.orderByDesc(Notification::getCreateTime);
        Page<Notification> result = notificationService.page(page, wrapper);
        return ApiResponse.success(result);
    }

    @Operation(summary = "标记通知为已读")
    @PostMapping("/read")
    public ApiResponse markAsRead(@RequestBody IdRequest idRequest) {
        Notification notification = notificationService.getById(idRequest.getId());
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }
        notification.setStatus(1);
        notification.setReadTime(LocalDateTime.now());
        notificationService.updateById(notification);
        return ApiResponse.success(true);
    }

    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread/count")
    public ApiResponse getUnreadCount(@RequestParam Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getStatus, 0);
        long count = notificationService.count(wrapper);
        return ApiResponse.success(count);
    }
}