package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairProcess;
import com.rosy.main.mapper.RepairProcessMapper;
import com.rosy.main.service.IRepairProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 维修过程记录表 服务实现类
 *
 * @author Rosy
 */
@Service
public class RepairProcessServiceImpl extends ServiceImpl<RepairProcessMapper, RepairProcess> implements IRepairProcessService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addProcess(Long orderId, Long operatorId, String operatorName, Byte actionType, String content) {
        RepairProcess process = new RepairProcess();
        process.setOrderId(orderId);
        process.setOperatorId(operatorId);
        process.setOperatorName(operatorName);
        process.setActionType(actionType);
        process.setContent(content);

        return save(process);
    }

    @Override
    public List<RepairProcess> getProcessByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairProcess> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairProcess::getOrderId, orderId)
                .orderByAsc(RepairProcess::getCreateTime);

        return list(wrapper);
    }
}
