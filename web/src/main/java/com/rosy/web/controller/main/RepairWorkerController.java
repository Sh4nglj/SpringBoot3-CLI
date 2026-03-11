package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairWorkerAddRequest;
import com.rosy.main.domain.dto.repair.RepairWorkerQueryRequest;
import com.rosy.main.domain.dto.repair.RepairWorkerUpdateRequest;
import com.rosy.main.domain.entity.RepairWorker;
import com.rosy.main.service.IRepairWorkerService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 维修人员 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/worker")
public class RepairWorkerController {
    @Resource
    IRepairWorkerService repairWorkerService;

    /**
     * 添加维修人员
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addWorker(@RequestBody RepairWorkerAddRequest request) {
        RepairWorker worker = new RepairWorker();
        BeanUtils.copyProperties(request, worker);
        boolean result = repairWorkerService.save(worker);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(worker.getId());
    }

    /**
     * 删除维修人员
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteWorker(@RequestBody IdRequest idRequest) {
        boolean result = repairWorkerService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新维修人员信息
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateWorker(@RequestBody RepairWorkerUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairWorker worker = new RepairWorker();
        BeanUtils.copyProperties(request, worker);
        boolean result = repairWorkerService.updateById(worker);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取维修人员详情
     */
    @GetMapping("/get")
    public ApiResponse getWorkerById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairWorker worker = repairWorkerService.getById(id);
        ThrowUtils.throwIf(worker == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(worker);
    }

    /**
     * 分页获取维修人员列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listWorkerByPage(@RequestBody RepairWorkerQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairWorker> workerPage = repairWorkerService.page(new Page<>(current, size), repairWorkerService.getQueryWrapper(request));
        return ApiResponse.success(workerPage);
    }

    /**
     * 获取可用维修人员列表（用于自动分配）
     */
    @GetMapping("/available")
    public ApiResponse getAvailableWorkers() {
        List<RepairWorker> workers = repairWorkerService.getAvailableWorkers();
        return ApiResponse.success(workers);
    }

    /**
     * 获取最空闲的维修人员
     */
    @GetMapping("/least-busy")
    public ApiResponse getLeastBusyWorker() {
        RepairWorker worker = repairWorkerService.getLeastBusyWorker();
        return ApiResponse.success(worker);
    }

    /**
     * 更新维修人员状态
     */
    @PostMapping("/status/update")
    @ValidateRequest
    public ApiResponse updateWorkerStatus(@RequestParam Long id, @RequestParam Integer status) {
        if (id == null || id <= 0 || status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = repairWorkerService.updateWorkerStatus(id, status);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }
}
