package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SecurityUtils;
import com.rosy.main.domain.vo.repair.NotificationVO;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notification")
@Tag(name = "通知管理", description = "通知相关接口")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    /**
     * 分页获取我的通知
     */
    @GetMapping("/list/my")
    @Operation(summary = "分页获取我的通知")
    public ApiResponse getMyNotifications(@RequestParam(defaultValue = "1") int current,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) Byte type) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<NotificationVO> page = notificationService.getUserNotifications(userId, current, pageSize, type);
        return ApiResponse.success(page);
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/mark-read")
    @Operation(summary = "标记通知为已读")
    public ApiResponse markAsRead(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = SecurityUtils.getCurrentUserId();
        boolean result = notificationService.markAsRead(id, userId);
        return ApiResponse.success(result);
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/mark-all-read")
    @Operation(summary = "标记所有通知为已读")
    public ApiResponse markAllAsRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        int count = notificationService.markAllAsRead(userId);
        return ApiResponse.success(count);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读通知数量")
    public ApiResponse getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        long count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }
}
