package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair")
public class RepairController {

    @Resource
    private IRepairOrderService repairOrderService;

    @Resource
    private IRepairRecordService repairRecordService;

    @PostMapping("/order/add")
    @ValidateRequest
    public ApiResponse addRepairOrder(@RequestBody RepairOrderAddRequest request) {
        Long orderId = repairOrderService.addRepairOrder(request);
        return ApiResponse.success(orderId);
    }

    @GetMapping("/order/get")
    public ApiResponse getRepairOrderById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrder repairOrder = repairOrderService.getById(id);
        ThrowUtils.throwIf(repairOrder == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(repairOrderService.getRepairOrderVO(repairOrder));
    }

    @PostMapping("/order/list/page")
    @ValidateRequest
    public ApiResponse listRepairOrderByPage(@RequestBody RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairOrder> page = repairOrderService.page(new Page<>(current, size),
                repairOrderService.getQueryWrapper(request));
        Page<RepairOrderVO> voPage = PageUtils.convert(page, repairOrderService::getRepairOrderVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/order/auto-assign")
    @ValidateRequest
    public ApiResponse autoAssign(@RequestBody IdRequest idRequest) {
        Boolean result = repairOrderService.autoAssign(idRequest.getId());
        return ApiResponse.success(result);
    }

    @Data
    public static class ManualAssignRequest {
        private Long id;
        private Long assigneeId;
    }

    @PostMapping("/order/manual-assign")
    public ApiResponse manualAssign(@RequestBody ManualAssignRequest request) {
        if (request.getId() == null || request.getAssigneeId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.manualAssign(request.getId(), request.getAssigneeId());
        return ApiResponse.success(result);
    }

    @PostMapping("/order/accept")
    @ValidateRequest
    public ApiResponse acceptOrder(@RequestBody IdRequest idRequest) {
        Boolean result = repairOrderService.acceptOrder(idRequest.getId());
        return ApiResponse.success(result);
    }

    @Data
    public static class CompleteOrderRequest {
        private Long id;
        private String repairResult;
    }

    @PostMapping("/order/complete")
    public ApiResponse completeOrder(@RequestBody CompleteOrderRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = repairOrderService.completeOrder(request.getId(), request.getRepairResult());
        return ApiResponse.success(result);
    }

    @PostMapping("/record/add")
    @ValidateRequest
    public ApiResponse addRepairRecord(@RequestBody RepairRecordAddRequest request) {
        Long recordId = repairRecordService.addRepairRecord(request);
        return ApiResponse.success(recordId);
    }

    @GetMapping("/record/list")
    public ApiResponse getRepairRecordList(Long repairOrderId) {
        if (repairOrderId == null || repairOrderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<RepairRecord> records = repairRecordService.getByRepairOrderId(repairOrderId);
        return ApiResponse.success(records);
    }
}
