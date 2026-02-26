package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.enums.RecordTypeEnum;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairRecordService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord repairRecord) {
        if (repairRecord == null) {
            return null;
        }
        RepairRecordVO vo = BeanUtil.copyProperties(repairRecord, RepairRecordVO.class);
        vo.setRecordTypeText(RecordTypeEnum.getDescByCode(repairRecord.getRecordType()));
        if (repairRecord.getRecordImages() != null) {
            vo.setRecordImages(JSON.parseArray(repairRecord.getRecordImages(), String.class));
        }
        return vo;
    }

    @Override
    public List<RepairRecordVO> getRepairRecordVOList(List<RepairRecord> repairRecords) {
        if (repairRecords == null || repairRecords.isEmpty()) {
            return Collections.emptyList();
        }
        return repairRecords.stream()
                .map(this::getRepairRecordVO)
                .toList();
    }

    @Override
    public List<RepairRecord> getByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairRecord::getOrderId, orderId)
                .orderByAsc(RepairRecord::getCreateTime);
        return this.list(queryWrapper);
    }
}
