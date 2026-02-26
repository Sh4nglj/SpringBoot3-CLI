package com.rosy.web.controller.repair;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.enums.RecordTypeEnum;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRecord(@RequestBody RepairRecordAddRequest addRequest) {
        RepairOrder order = repairOrderService.getById(addRequest.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        RepairRecord record = BeanUtil.copyProperties(addRequest, RepairRecord.class);
        record.setRepairerId(1L);
        if (addRequest.getRecordImages() != null && !addRequest.getRecordImages().isEmpty()) {
            record.setRecordImages(JSON.toJSONString(addRequest.getRecordImages()));
        }
        if (RecordTypeEnum.REPAIR_COMPLETE.getCode().equals(addRequest.getRecordType())) {
            repairOrderService.completeOrder(addRequest.getOrderId());
        }
        boolean result = repairRecordService.save(record);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加维修记录失败");
        return ApiResponse.success(record.getId());
    }

    @GetMapping("/list")
    public ApiResponse listRecordsByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<RepairRecord> records = repairRecordService.getByOrderId(orderId);
        return ApiResponse.success(repairRecordService.getRepairRecordVOList(records));
    }
}
