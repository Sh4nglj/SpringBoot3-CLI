package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairProcessAddRequest;
import com.rosy.main.domain.entity.RepairProcess;
import com.rosy.main.domain.vo.RepairProcessVO;
import com.rosy.main.service.IRepairProcessService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修过程记录 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/process")
public class RepairProcessController {
    @Resource
    IRepairProcessService repairProcessService;

    /**
     * 添加维修过程记录
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addProcess(@RequestBody RepairProcessAddRequest request) {
        Long processId = repairProcessService.addProcess(request);
        return ApiResponse.success(processId);
    }

    /**
     * 删除维修过程记录
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteProcess(@RequestBody IdRequest idRequest) {
        boolean result = repairProcessService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 获取订单的维修过程记录列表
     */
    @GetMapping("/list")
    public ApiResponse getProcessesByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairProcess> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairProcess::getOrderId, orderId)
                .orderByAsc(RepairProcess::getCreateTime);
        List<RepairProcess> processes = repairProcessService.list(wrapper);
        List<RepairProcessVO> processVOs = processes.stream()
                .map(repairProcessService::getRepairProcessVO)
                .collect(Collectors.toList());
        return ApiResponse.success(processVOs);
    }

    /**
     * 根据ID获取维修过程记录详情
     */
    @GetMapping("/get")
    public ApiResponse getProcessById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairProcess process = repairProcessService.getById(id);
        ThrowUtils.throwIf(process == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(process);
    }
}
