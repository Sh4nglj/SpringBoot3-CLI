package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.RepairEvaluationVO;

public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation repairEvaluation);

    RepairEvaluation getByOrderId(Long orderId);
}
