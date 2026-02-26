package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 维修评价表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface IRepairEvaluationService extends IService<RepairEvaluation> {

    /**
     * 校验数据
     *
     * @param repairEvaluation
     * @param add              是否为创建校验
     */
    void validRepairEvaluation(RepairEvaluation repairEvaluation, boolean add);

    /**
     * 获取评价VO
     *
     * @param repairEvaluation
     * @return
     */
    RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation repairEvaluation);

    /**
     * 添加评价
     *
     * @param repairEvaluationAddRequest
     * @param request
     * @return
     */
    Long addRepairEvaluation(RepairEvaluationAddRequest repairEvaluationAddRequest, HttpServletRequest request);

    /**
     * 根据报修单ID获取评价
     *
     * @param orderId
     * @return
     */
    RepairEvaluationVO getRepairEvaluationByOrderId(Long orderId);
}
