package com.rosy.web.controller.repair;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 报修单 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    // region 用户相关接口

    /**
     * 用户提交报修申请
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairOrder(@RequestBody RepairOrderAddRequest repairOrderAddRequest,
                                      HttpServletRequest request) {
        Long orderId = repairOrderService.addRepairOrder(repairOrderAddRequest, request);
        return ApiResponse.success(orderId);
    }

    /**
     * 用户修改报修单（仅限待处理状态）
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRepairOrder(@RequestBody RepairOrderUpdateRequest repairOrderUpdateRequest,
                                         HttpServletRequest request) {
        if (repairOrderUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.updateRepairOrder(repairOrderUpdateRequest, request);
        return ApiResponse.success(result);
    }

    /**
     * 用户取消报修单
     */
    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelRepairOrder(@RequestBody IdRequest idRequest,
                                         HttpServletRequest request) {
        if (idRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.cancelRepairOrder(idRequest.getId(), request);
        return ApiResponse.success(result);
    }

    /**
     * 用户查询自己的报修单列表
     */
    @PostMapping("/my/list")
    public ApiResponse listMyRepairOrders(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest,
                                          HttpServletRequest request) {
        // TODO: 从request中获取当前登录用户ID
        Long currentUserId = 1L;
        repairOrderQueryRequest.setUserId(currentUserId);

        Page<RepairOrder> repairOrderPage = repairOrderService.page(
                new Page<>(repairOrderQueryRequest.getCurrent(), repairOrderQueryRequest.getPageSize()),
                repairOrderService.getQueryWrapper(repairOrderQueryRequest)
        );
        return ApiResponse.success(repairOrderService.getRepairOrderVOPage(repairOrderPage));
    }

    // endregion

    // region 管理员相关接口

    /**
     * 管理员查询所有报修单
     */
    @PostMapping("/list")
    public ApiResponse listRepairOrders(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest) {
        Page<RepairOrder> repairOrderPage = repairOrderService.page(
                new Page<>(repairOrderQueryRequest.getCurrent(), repairOrderQueryRequest.getPageSize()),
                repairOrderService.getQueryWrapper(repairOrderQueryRequest)
        );
        return ApiResponse.success(repairOrderService.getRepairOrderVOPage(repairOrderPage));
    }

    /**
     * 管理员手动分配报修单
     */
    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignRepairOrder(@RequestBody RepairOrderAssignRequest repairOrderAssignRequest,
                                         HttpServletRequest request) {
        Boolean result = repairOrderService.assignRepairOrder(repairOrderAssignRequest, request);
        return ApiResponse.success(result);
    }

    /**
     * 管理员自动分配报修单
     */
    @PostMapping("/auto-assign")
    @ValidateRequest
    public ApiResponse autoAssignRepairOrder(@RequestBody IdRequest idRequest,
                                             HttpServletRequest request) {
        if (idRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.autoAssignRepairOrder(idRequest.getId());
        return ApiResponse.success(result);
    }

    // endregion

    // region 维修人员相关接口

    /**
     * 维修人员接单/拒单
     */
    @PostMapping("/accept")
    @ValidateRequest
    public ApiResponse acceptRepairOrder(@RequestBody RepairOrderAcceptRequest repairOrderAcceptRequest,
                                         HttpServletRequest request) {
        Boolean result = repairOrderService.acceptRepairOrder(repairOrderAcceptRequest, request);
        return ApiResponse.success(result);
    }

    /**
     * 维修人员完成报修单
     */
    @PostMapping("/complete")
    @ValidateRequest
    public ApiResponse completeRepairOrder(@RequestBody IdRequest idRequest,
                                           HttpServletRequest request) {
        if (idRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.completeRepairOrder(idRequest.getId(), request);
        return ApiResponse.success(result);
    }

    /**
     * 维修人员查询分配给自己的报修单
     */
    @PostMapping("/repairman/list")
    public ApiResponse listRepairmanOrders(@RequestBody RepairOrderQueryRequest repairOrderQueryRequest,
                                           HttpServletRequest request) {
        // TODO: 从request中获取当前登录维修人员ID
        Long currentRepairmanId = 1L;
        repairOrderQueryRequest.setRepairmanId(currentRepairmanId);

        Page<RepairOrder> repairOrderPage = repairOrderService.page(
                new Page<>(repairOrderQueryRequest.getCurrent(), repairOrderQueryRequest.getPageSize()),
                repairOrderService.getQueryWrapper(repairOrderQueryRequest)
        );
        return ApiResponse.success(repairOrderService.getRepairOrderVOPage(repairOrderPage));
    }

    // endregion

    // region 通用接口

    /**
     * 根据ID获取报修单详情
     */
    @GetMapping("/get")
    public ApiResponse getRepairOrderById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairOrderService.getRepairOrderVO(repairOrder));
    }

    // endregion
}
