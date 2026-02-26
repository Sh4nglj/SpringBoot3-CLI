package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;

import java.util.List;

public interface IRepairRecordService extends IService<RepairRecord> {

    RepairRecordVO getRepairRecordVO(RepairRecord repairRecord);

    List<RepairRecordVO> getRepairRecordVOList(List<RepairRecord> repairRecords);

    List<RepairRecord> getByOrderId(Long orderId);
}
