package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairCompleteRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderCreateRequest;
import com.rosy.main.domain.dto.repair.RepairRatingRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 报修订单 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {
    @Resource
    IRepairOrderService repairOrderService;

    /**
     * 创建报修订单
     */
    @PostMapping("/create")
    @ValidateRequest
    public ApiResponse createOrder(@RequestBody RepairOrderCreateRequest request) {
        Long orderId = repairOrderService.createOrder(request);
        return ApiResponse.success(orderId);
    }

    /**
     * 手动分配订单
     */
    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest request) {
        boolean result = repairOrderService.assignOrder(request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 自动分配订单
     */
    @PostMapping("/auto-assign")
    @ValidateRequest
    public ApiResponse autoAssignOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.autoAssignOrder(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 维修人员接单
     */
    @PostMapping("/accept")
    @ValidateRequest
    public ApiResponse acceptOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.acceptOrder(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 完成维修
     */
    @PostMapping("/complete")
    @ValidateRequest
    public ApiResponse completeRepair(@RequestBody RepairCompleteRequest request) {
        boolean result = repairOrderService.completeRepair(request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 用户评价
     */
    @PostMapping("/rate")
    @ValidateRequest
    public ApiResponse rateOrder(@RequestBody RepairRatingRequest request) {
        boolean result = repairOrderService.rateOrder(request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.cancelOrder(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取订单详情
     */
    @GetMapping("/get")
    public ApiResponse getOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder order = repairOrderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(order);
    }

    /**
     * 根据 id 获取订单VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getOrderVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder order = repairOrderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairOrderService.getRepairOrderVO(order));
    }

    /**
     * 分页获取订单列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listOrderByPage(@RequestBody com.rosy.main.domain.dto.repair.RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairOrder> orderPage = repairOrderService.page(new Page<>(current, size), repairOrderService.getQueryWrapper(request));
        return ApiResponse.success(orderPage);
    }

    /**
     * 分页获取订单VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listOrderVOByPage(@RequestBody com.rosy.main.domain.dto.repair.RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<RepairOrder> orderPage = repairOrderService.page(new Page<>(current, size), repairOrderService.getQueryWrapper(request));
        Page<RepairOrderVO> orderVOPage = PageUtils.convert(orderPage, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(orderVOPage);
    }

    /**
     * 获取用户的订单列表
     */
    @GetMapping("/user/list")
    public ApiResponse getUserOrders(@RequestParam Long userId,
                                     @RequestParam(defaultValue = "1") long current,
                                     @RequestParam(defaultValue = "10") long size) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<RepairOrder> orderPage = repairOrderService.getUserOrders(userId, current, size);
        Page<RepairOrderVO> orderVOPage = PageUtils.convert(orderPage, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(orderVOPage);
    }

    /**
     * 获取维修人员的订单列表
     */
    @GetMapping("/worker/list")
    public ApiResponse getWorkerOrders(@RequestParam Long workerId,
                                       @RequestParam(defaultValue = "1") long current,
                                       @RequestParam(defaultValue = "10") long size) {
        if (workerId == null || workerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<RepairOrder> orderPage = repairOrderService.getWorkerOrders(workerId, current, size);
        Page<RepairOrderVO> orderVOPage = PageUtils.convert(orderPage, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(orderVOPage);
    }
}
