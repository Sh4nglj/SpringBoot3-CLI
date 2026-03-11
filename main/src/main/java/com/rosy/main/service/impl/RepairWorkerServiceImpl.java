package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.repair.RepairWorkerQueryRequest;
import com.rosy.main.domain.entity.RepairWorker;
import com.rosy.main.mapper.RepairWorkerMapper;
import com.rosy.main.service.IRepairWorkerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 维修人员表 服务实现类
 *
 * @author Rosy
 */
@Service
public class RepairWorkerServiceImpl extends ServiceImpl<RepairWorkerMapper, RepairWorker> implements IRepairWorkerService {

    @Override
    public List<RepairWorker> getAvailableWorkers() {
        LambdaQueryWrapper<RepairWorker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairWorker::getStatus, 1) // 在线
                .orderByAsc(RepairWorker::getOrderCount);

        return list(wrapper);
    }

    @Override
    public RepairWorker getLeastBusyWorker() {
        LambdaQueryWrapper<RepairWorker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairWorker::getStatus, 1) // 在线
                .orderByAsc(RepairWorker::getOrderCount)
                .last("LIMIT 1");

        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateWorkerStatus(Long workerId, Byte status) {
        LambdaUpdateWrapper<RepairWorker> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RepairWorker::getId, workerId)
                .set(RepairWorker::getStatus, status);

        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean incrementOrderCount(Long workerId) {
        LambdaUpdateWrapper<RepairWorker> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RepairWorker::getId, workerId)
                .setSql("order_count = order_count + 1");

        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean decrementOrderCount(Long workerId) {
        LambdaUpdateWrapper<RepairWorker> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RepairWorker::getId, workerId)
                .setSql("order_count = order_count - 1");

        return update(wrapper);
    }

    @Override
    public RepairWorker getByUserId(Long userId) {
        LambdaQueryWrapper<RepairWorker> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairWorker::getUserId, userId);

        return getOne(wrapper);
    }

    @Override
    public LambdaQueryWrapper<RepairWorker> getQueryWrapper(RepairWorkerQueryRequest request) {
        LambdaQueryWrapper<RepairWorker> wrapper = new LambdaQueryWrapper<>();
        if (request == null) {
            return wrapper;
        }

        // 姓名
        if (request.getName() != null && !request.getName().isEmpty()) {
            wrapper.like(RepairWorker::getName, request.getName());
        }

        // 手机号
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            wrapper.like(RepairWorker::getPhone, request.getPhone());
        }

        // 专长
        if (request.getSpecialty() != null && !request.getSpecialty().isEmpty()) {
            wrapper.like(RepairWorker::getSpecialty, request.getSpecialty());
        }

        // 状态
        if (request.getStatus() != null) {
            wrapper.eq(RepairWorker::getStatus, request.getStatus());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(RepairWorker::getCreateTime);

        return wrapper;
    }
}
