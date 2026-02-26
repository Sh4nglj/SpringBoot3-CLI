package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairRecord;

import java.util.List;

public interface IRepairRecordService extends IService<RepairRecord> {

    Long addRepairRecord(RepairRecordAddRequest request);

    List<RepairRecord> getByRepairOrderId(Long repairOrderId);
}
