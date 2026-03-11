package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.RepairNotification;
import com.rosy.main.service.IRepairNotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 通知消息 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/notification")
public class RepairNotificationController {
    @Resource
    IRepairNotificationService repairNotificationService;

    /**
     * 获取用户的通知列表
     */
    @GetMapping("/user/list")
    public ApiResponse getUserNotifications(@RequestParam Long userId,
                                            @RequestParam(defaultValue = "1") long current,
                                            @RequestParam(defaultValue = "10") long size) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairNotification::getUserId, userId)
                .orderByDesc(RepairNotification::getCreateTime);
        Page<RepairNotification> notificationPage = repairNotificationService.page(new Page<>(current, size), wrapper);
        return ApiResponse.success(notificationPage);
    }

    /**
     * 获取维修人员的通知列表
     */
    @GetMapping("/worker/list")
    public ApiResponse getWorkerNotifications(@RequestParam Long workerId,
                                              @RequestParam(defaultValue = "1") long current,
                                              @RequestParam(defaultValue = "10") long size) {
        if (workerId == null || workerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairNotification::getWorkerId, workerId)
                .orderByDesc(RepairNotification::getCreateTime);
        Page<RepairNotification> notificationPage = repairNotificationService.page(new Page<>(current, size), wrapper);
        return ApiResponse.success(notificationPage);
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/read")
    public ApiResponse markAsRead(@RequestBody IdRequest idRequest) {
        boolean result = repairNotificationService.markAsRead(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 批量标记用户通知为已读
     */
    @PostMapping("/read/user/batch")
    public ApiResponse markUserNotificationsAsRead(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = repairNotificationService.markUserNotificationsAsRead(userId);
        return ApiResponse.success(result);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread/count")
    public ApiResponse getUnreadCount(@RequestParam(required = false) Long userId,
                                      @RequestParam(required = false) Long workerId) {
        if (userId == null && workerId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long count = repairNotificationService.getUnreadCount(userId, workerId);
        return ApiResponse.success(count);
    }

    /**
     * 根据ID获取通知详情
     */
    @GetMapping("/get")
    public ApiResponse getNotificationById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairNotification notification = repairNotificationService.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(notification);
    }
}
