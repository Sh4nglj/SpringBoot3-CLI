package com.rosy.web.controller.repair;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 维修记录 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/repair/record")
public class RepairRecordController {

    @Resource
    private IRepairRecordService repairRecordService;

    /**
     * 维修人员添加维修记录
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addRepairRecord(@RequestBody RepairRecordAddRequest repairRecordAddRequest,
                                       HttpServletRequest request) {
        Long recordId = repairRecordService.addRepairRecord(repairRecordAddRequest, request);
        return ApiResponse.success(recordId);
    }

    /**
     * 根据报修单ID获取维修记录列表
     */
    @GetMapping("/list")
    public ApiResponse getRepairRecordsByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<RepairRecordVO> repairRecordVOList = repairRecordService.getRepairRecordsByOrderId(orderId);
        return ApiResponse.success(repairRecordVOList);
    }

    /**
     * 根据ID获取维修记录详情
     */
    @GetMapping("/get")
    public ApiResponse getRepairRecordById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairRecordVO repairRecordVO = repairRecordService.getRepairRecordVO(repairRecordService.getById(id));
        return ApiResponse.success(repairRecordVO);
    }
}
