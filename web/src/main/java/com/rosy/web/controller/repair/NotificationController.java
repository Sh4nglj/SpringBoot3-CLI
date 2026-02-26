package com.rosy.web.controller.repair;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.service.INotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    @GetMapping("/unread/count")
    public ApiResponse getUnreadCount() {
        Long userId = 1L;
        Long count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }

    @GetMapping("/list/page")
    public ApiResponse listByPage(@RequestParam(defaultValue = "1") long current,
                                   @RequestParam(defaultValue = "10") long size) {
        Long userId = 1L;
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreateTime);
        Page<Notification> page = notificationService.page(new Page<>(current, size), queryWrapper);
        Page<NotificationVO> voPage = PageUtils.convert(page, this::getNotificationVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/read")
    @ValidateRequest
    public ApiResponse markAsRead(@RequestBody IdRequest idRequest) {
        Long userId = 1L;
        notificationService.markAsRead(idRequest.getId(), userId);
        return ApiResponse.success(true);
    }

    @PostMapping("/read/all")
    public ApiResponse markAllAsRead() {
        Long userId = 1L;
        notificationService.lambdaUpdate()
                .eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)
                .set(Notification::getIsRead, 1)
                .update();
        return ApiResponse.success(true);
    }

    private NotificationVO getNotificationVO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationVO vo = BeanUtil.copyProperties(notification, NotificationVO.class);
        vo.setTypeText(NotificationTypeEnum.values()[notification.getType() - 1].getDesc());
        return vo;
    }
}
