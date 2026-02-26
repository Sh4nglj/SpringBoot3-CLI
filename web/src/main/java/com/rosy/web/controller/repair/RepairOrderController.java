package com.rosy.web.controller.repair;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairOrderAcceptRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderCompleteRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.enums.RepairOrderPriorityEnum;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addOrder(@RequestBody RepairOrderAddRequest addRequest, HttpServletRequest request) {
        RepairOrder order = BeanUtil.copyProperties(addRequest, RepairOrder.class);
        order.setOrderNo(repairOrderService.generateOrderNo());
        order.setStatus(RepairOrderStatusEnum.PENDING.getCode());
        order.setPriority(RepairOrderPriorityEnum.MEDIUM.getCode());
        if (addRequest.getUserId() == null) {
            order.setUserId(1L);
        }
        if (addRequest.getFaultImages() != null && !addRequest.getFaultImages().isEmpty()) {
            order.setFaultImages(JSON.toJSONString(addRequest.getFaultImages()));
        }
        boolean result = repairOrderService.save(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建报修工单失败");
        return ApiResponse.success(order.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteOrder(@RequestBody IdRequest idRequest) {
        boolean result = repairOrderService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest assignRequest) {
        repairOrderService.assignOrder(assignRequest, false);
        return ApiResponse.success(true);
    }

    @PostMapping("/auto-assign")
    @ValidateRequest
    public ApiResponse autoAssignOrder(@RequestBody IdRequest idRequest) {
        repairOrderService.autoAssignOrder(idRequest.getId());
        return ApiResponse.success(true);
    }

    @PostMapping("/accept")
    @ValidateRequest
    public ApiResponse acceptOrder(@RequestBody RepairOrderAcceptRequest acceptRequest, HttpServletRequest request) {
        Long repairerId = 1L;
        repairOrderService.acceptOrder(acceptRequest.getOrderId(), repairerId);
        return ApiResponse.success(true);
    }

    @PostMapping("/complete")
    @ValidateRequest
    public ApiResponse completeOrder(@RequestBody RepairOrderCompleteRequest completeRequest) {
        repairOrderService.completeOrderWithResult(
                completeRequest.getOrderId(),
                completeRequest.getRepairResult(),
                completeRequest.getRepairImages(),
                completeRequest.getRepairCost()
        );
        return ApiResponse.success(true);
    }

    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelOrder(@RequestBody IdRequest idRequest) {
        repairOrderService.cancelOrder(idRequest.getId());
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getOrderById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder order = repairOrderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        return ApiResponse.success(order);
    }

    @GetMapping("/get/vo")
    public ApiResponse getOrderVOById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder order = repairOrderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        return ApiResponse.success(repairOrderService.getRepairOrderVO(order));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listOrderByPage(@RequestBody RepairOrderQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<RepairOrder> orderPage = repairOrderService.page(
                new Page<>(current, size),
                repairOrderService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(orderPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listOrderVOByPage(@RequestBody RepairOrderQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<RepairOrder> orderPage = repairOrderService.page(
                new Page<>(current, size),
                repairOrderService.getQueryWrapper(queryRequest)
        );
        Page<RepairOrderVO> orderVOPage = PageUtils.convert(orderPage, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(orderVOPage);
    }
}
