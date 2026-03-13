package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairWorkerQueryRequest;
import com.rosy.main.domain.entity.RepairWorker;

import java.util.List;

/**
 * 维修人员表 服务类
 *
 * @author Rosy
 */
public interface IRepairWorkerService extends IService<RepairWorker> {

    /**
     * 获取可用的维修人员列表
     *
     * @return 维修人员列表
     */
    List<RepairWorker> getAvailableWorkers();

    /**
     * 获取最空闲的维修人员
     *
     * @return 维修人员
     */
    RepairWorker getLeastBusyWorker();

    /**
     * 更新维修人员状态
     *
     * @param workerId 维修人员ID
     * @param status   状态
     * @return 是否成功
     */
    Boolean updateWorkerStatus(Long workerId, Byte status);

    /**
     * 增加维修人员工单数量
     *
     * @param workerId 维修人员ID
     * @return 是否成功
     */
    Boolean incrementOrderCount(Long workerId);

    /**
     * 减少维修人员工单数量
     *
     * @param workerId 维修人员ID
     * @return 是否成功
     */
    Boolean decrementOrderCount(Long workerId);

    /**
     * 根据用户ID获取维修人员信息
     *
     * @param userId 用户ID
     * @return 维修人员信息
     */
    RepairWorker getByUserId(Long userId);

    /**
     * 获取查询条件
     *
     * @param request 查询请求
     * @return 查询条件
     */
    LambdaQueryWrapper<RepairWorker> getQueryWrapper(RepairWorkerQueryRequest request);
}
