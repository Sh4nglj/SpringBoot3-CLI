package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;

public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    Long addEvaluation(RepairEvaluationAddRequest request);

    RepairEvaluation getByRepairOrderId(Long repairOrderId);
}
