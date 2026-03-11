package com.rosy.common.utils;

import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 安全工具类
 */
public class SecurityUtils {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        HttpServletRequest request = attributes.getRequest();
        String userIdStr = request.getHeader(USER_ID_HEADER);
        if (StringUtils.isBlank(userIdStr)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
    }

    /**
     * 获取当前用户角色
     */
    public static String getCurrentUserRole() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(USER_ROLE_HEADER);
    }

    /**
     * 检查是否为管理员
     */
    public static boolean isAdmin() {
        String role = getCurrentUserRole();
        return "admin".equals(role);
    }

    /**
     * 检查是否为维修人员
     */
    public static boolean isRepairer() {
        String role = getCurrentUserRole();
        return "repair".equals(role);
    }

    /**
     * 获取当前用户ID（可选）
     */
    public static Optional<Long> getCurrentUserIdOptional() {
        try {
            return Optional.of(getCurrentUserId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
