package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Override
    public Long addRepairRecord(RepairRecordAddRequest request) {
        RepairRecord repairRecord = BeanUtil.copyProperties(request, RepairRecord.class);
        this.save(repairRecord);
        return repairRecord.getId();
    }

    @Override
    public List<RepairRecord> getByRepairOrderId(Long repairOrderId) {
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRecord::getRepairOrderId, repairOrderId);
        queryWrapper.orderByAsc(RepairRecord::getCreateTime);
        return this.list(queryWrapper);
    }
}
