package com.rosy.main.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Repairer;
import com.rosy.main.mapper.RepairerMapper;
import com.rosy.main.service.IRepairerService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RepairerServiceImpl extends ServiceImpl<RepairerMapper, Repairer> implements IRepairerService {

    @Override
    public Repairer findBestAvailableRepairer(String deviceType) {
        LambdaQueryWrapper<Repairer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Repairer::getStatus, 1)
                .apply("workload < max_workload");
        if (StrUtil.isNotBlank(deviceType)) {
            queryWrapper.like(Repairer::getSpecialty, deviceType);
        }
        queryWrapper.orderByAsc(Repairer::getWorkload);
        List<Repairer> repairers = this.list(queryWrapper);
        if (repairers == null || repairers.isEmpty()) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Repairer::getStatus, 1)
                    .apply("workload < max_workload")
                    .orderByAsc(Repairer::getWorkload);
            repairers = this.list(queryWrapper);
        }
        if (repairers == null || repairers.isEmpty()) {
            return null;
        }
        return repairers.stream()
                .min(Comparator.comparingInt(Repairer::getWorkload))
                .orElse(null);
    }

    @Override
    public void incrementWorkload(Long repairerId) {
        LambdaUpdateWrapper<Repairer> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Repairer::getId, repairerId)
                .setSql("workload = workload + 1");
        this.update(updateWrapper);
    }

    @Override
    public void decrementWorkload(Long repairerId) {
        LambdaUpdateWrapper<Repairer> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Repairer::getId, repairerId)
                .gt(Repairer::getWorkload, 0)
                .setSql("workload = workload - 1");
        this.update(updateWrapper);
    }
}
