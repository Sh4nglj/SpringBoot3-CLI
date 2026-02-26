package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.service.IRepairEvaluationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    @Override
    public RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation repairEvaluation) {
        return Optional.ofNullable(repairEvaluation)
                .map(e -> BeanUtil.copyProperties(e, RepairEvaluationVO.class))
                .orElse(null);
    }

    @Override
    public RepairEvaluation getByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairEvaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepairEvaluation::getOrderId, orderId);
        return this.getOne(queryWrapper);
    }
}
