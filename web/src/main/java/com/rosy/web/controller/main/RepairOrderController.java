package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SecurityUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.vo.repair.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 报修单控制器
 */
@RestController
@RequestMapping("/repair/order")
@Tag(name = "报修单管理", description = "报修单相关接口")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    /**
     * 提交报修申请
     */
    @PostMapping("/submit")
    @ValidateRequest
    @Operation(summary = "提交报修申请")
    public ApiResponse submitOrder(@RequestBody RepairOrderSubmitRequest request) {
        Long orderId = repairOrderService.submitRepairOrder(request);
        return ApiResponse.success(orderId);
    }

    /**
     * 自动分配工单
     */
    @PostMapping("/auto-assign")
    @Operation(summary = "自动分配工单")
    public ApiResponse autoAssign(@RequestParam Long orderId) {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅管理员可分配工单");
        }
        boolean result = repairOrderService.autoAssignOrder(orderId);
        return ApiResponse.success(result);
    }

    /**
     * 手动分配工单
     */
    @PostMapping("/manual-assign")
    @ValidateRequest
    @Operation(summary = "手动分配工单")
    public ApiResponse manualAssign(@RequestBody RepairOrderAssignRequest request) {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅管理员可分配工单");
        }
        boolean result = repairOrderService.manualAssignOrder(request);
        return ApiResponse.success(result);
    }

    /**
     * 维修人员接单
     */
    @PostMapping("/accept")
    @Operation(summary = "接单")
    public ApiResponse acceptOrder(@RequestParam Long orderId) {
        if (!SecurityUtils.isRepairer()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅维修人员可接单");
        }
        Long repairerId = SecurityUtils.getCurrentUserId();
        boolean result = repairOrderService.acceptOrder(orderId, repairerId);
        return ApiResponse.success(result);
    }

    /**
     * 完成维修
     */
    @PostMapping("/complete-repair")
    @ValidateRequest
    @Operation(summary = "完成维修")
    public ApiResponse completeRepair(@RequestBody CompleteRepairRequest request) {
        if (!SecurityUtils.isRepairer()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅维修人员可完成维修");
        }
        Long repairerId = SecurityUtils.getCurrentUserId();
        boolean result = repairOrderService.completeRepair(request, repairerId);
        return ApiResponse.success(result);
    }

    /**
     * 用户确认完成
     */
    @PostMapping("/confirm-complete")
    @Operation(summary = "确认完成")
    public ApiResponse confirmComplete(@RequestParam Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean result = repairOrderService.confirmComplete(orderId, userId);
        return ApiResponse.success(result);
    }

    /**
     * 取消工单
     */
    @PostMapping("/cancel")
    @Operation(summary = "取消工单")
    public ApiResponse cancelOrder(@RequestParam Long orderId) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean result = repairOrderService.cancelOrder(orderId, userId);
        return ApiResponse.success(result);
    }

    /**
     * 设置优先级
     */
    @PostMapping("/set-priority")
    @Operation(summary = "设置优先级")
    public ApiResponse setPriority(@RequestParam Long orderId, @RequestParam Byte priority) {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅管理员可设置优先级");
        }
        boolean result = repairOrderService.setPriority(orderId, priority);
        return ApiResponse.success(result);
    }

    /**
     * 获取工单详情
     */
    @GetMapping("/detail")
    @Operation(summary = "获取工单详情")
    public ApiResponse getOrderDetail(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.getOrderDetail(id);
        ThrowUtils.throwIf(orderVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(orderVO);
    }

    /**
     * 分页查询所有工单（管理员）
     */
    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页查询所有工单")
    public ApiResponse listOrderByPage(@RequestBody RepairOrderQueryRequest request) {
        if (!SecurityUtils.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅管理员可查询所有工单");
        }
        Page<RepairOrderVO> page = repairOrderService.pageOrder(request);
        return ApiResponse.success(page);
    }

    /**
     * 分页查询我的工单（用户）
     */
    @PostMapping("/list/my")
    @ValidateRequest
    @Operation(summary = "分页查询我的工单")
    public ApiResponse listMyOrder(@RequestBody RepairOrderQueryRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<RepairOrderVO> page = repairOrderService.pageUserOrder(userId, request);
        return ApiResponse.success(page);
    }

    /**
     * 分页查询维修人员工单
     */
    @PostMapping("/list/repairer")
    @ValidateRequest
    @Operation(summary = "分页查询维修人员工单")
    public ApiResponse listRepairerOrder(@RequestBody RepairOrderQueryRequest request) {
        if (!SecurityUtils.isRepairer()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅维修人员可查询");
        }
        Long repairerId = SecurityUtils.getCurrentUserId();
        Page<RepairOrderVO> page = repairOrderService.pageRepairerOrder(repairerId, request);
        return ApiResponse.success(page);
    }
}
