package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SecurityUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 维修记录控制器
 */
@RestController
@RequestMapping("/repair/record")
@Tag(name = "维修记录管理", description = "维修记录相关接口")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    /**
     * 添加维修记录
     */
    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "添加维修记录")
    public ApiResponse addRecord(@RequestBody RepairRecordAddRequest request) {
        if (!SecurityUtils.isRepairer()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅维修人员可添加维修记录");
        }
        Long repairerId = SecurityUtils.getCurrentUserId();
        Long recordId = repairRecordService.addRecord(request, repairerId);
        return ApiResponse.success(recordId);
    }

    /**
     * 根据工单ID查询维修记录列表
     */
    @GetMapping("/list/order")
    @Operation(summary = "根据工单ID查询维修记录")
    public ApiResponse getRecordsByOrderId(@RequestParam Long orderId) {
        if (orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<RepairRecordVO> records = repairRecordService.getRecordsByOrderId(orderId);
        return ApiResponse.success(records);
    }

    /**
     * 分页查询我的维修记录
     */
    @GetMapping("/list/my")
    @Operation(summary = "分页查询我的维修记录")
    public ApiResponse listMyRecords(@RequestParam(defaultValue = "1") int current,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        if (!SecurityUtils.isRepairer()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅维修人员可查询");
        }
        Long repairerId = SecurityUtils.getCurrentUserId();
        Page<RepairRecordVO> page = repairRecordService.pageRecords(repairerId, current, pageSize);
        return ApiResponse.success(page);
    }
}
