package com.rosy.web.controller.repair;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.RepairmanVO;
import com.rosy.main.service.IRepairmanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 维修人员 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/repairman")
public class RepairmanController {

    @Resource
    private IRepairmanService repairmanService;

    /**
     * 获取所有可用的维修人员
     */
    @GetMapping("/available/list")
    public ApiResponse listAvailableRepairmen() {
        List<RepairmanVO> repairmanVOList = repairmanService.getAvailableRepairmen();
        return ApiResponse.success(repairmanVOList);
    }

    /**
     * 根据技能获取可用的维修人员
     */
    @GetMapping("/available/by-skill")
    public ApiResponse listAvailableRepairmenBySkill(@RequestParam String skill) {
        List<RepairmanVO> repairmanVOList = repairmanService.getAvailableRepairmenBySkill(skill);
        return ApiResponse.success(repairmanVOList);
    }

    /**
     * 根据ID获取维修人员详情
     */
    @GetMapping("/get")
    public ApiResponse getRepairmanById(@RequestParam Long id) {
        RepairmanVO repairmanVO = repairmanService.getRepairmanVO(repairmanService.getById(id));
        return ApiResponse.success(repairmanVO);
    }
}
