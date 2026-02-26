package com.rosy.main.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairEvaluationAddRequest;
import com.rosy.main.domain.entity.RepairEvaluation;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.service.IRepairEvaluationService;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairmanService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 维修评价表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Service
public class RepairEvaluationServiceImpl extends ServiceImpl<RepairEvaluationMapper, RepairEvaluation> implements IRepairEvaluationService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Resource
    private IRepairmanService repairmanService;

    @Override
    public void validRepairEvaluation(RepairEvaluation repairEvaluation, boolean add) {
        if (repairEvaluation == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long orderId = repairEvaluation.getOrderId();
        Integer rating = repairEvaluation.getRating();

        // 创建时，参数不能为空
        if (add) {
            if (orderId == null || orderId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "报修单ID不能为空");
            }
            if (rating == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "评分不能为空");
            }
        }

        // 有参数则校验
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评分必须在1-5之间");
        }
        if (StrUtil.isNotBlank(repairEvaluation.getContent()) && repairEvaluation.getContent().length() > 1000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价内容过长");
        }
    }

    @Override
    public RepairEvaluationVO getRepairEvaluationVO(RepairEvaluation repairEvaluation) {
        if (repairEvaluation == null) {
            return null;
        }
        RepairEvaluationVO repairEvaluationVO = new RepairEvaluationVO();
        BeanUtils.copyProperties(repairEvaluation, repairEvaluationVO);
        return repairEvaluationVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRepairEvaluation(RepairEvaluationAddRequest repairEvaluationAddRequest, HttpServletRequest request) {
        Long orderId = repairEvaluationAddRequest.getOrderId();

        // 检查报修单是否存在
        RepairOrder repairOrder = repairOrderService.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.COMPLETED.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单未完成，无法评价");
        }

        // 检查是否已评价
        QueryWrapper<RepairEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单已评价");
        }

        // TODO: 从request中获取当前登录用户ID，验证是否为报修用户
        Long currentUserId = 1L;
        if (!repairOrder.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您不是该报修单的报修用户");
        }

        RepairEvaluation repairEvaluation = new RepairEvaluation();
        BeanUtils.copyProperties(repairEvaluationAddRequest, repairEvaluation);
        repairEvaluation.setUserId(currentUserId);
        repairEvaluation.setRepairmanId(repairOrder.getRepairmanId());

        validRepairEvaluation(repairEvaluation, true);
        boolean result = this.save(repairEvaluation);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加评价失败");
        }

        // 更新维修人员平均评分
        if (repairOrder.getRepairmanId() != null) {
            repairmanService.updateAverageRating(repairOrder.getRepairmanId(), repairEvaluation.getRating());
        }

        return repairEvaluation.getId();
    }

    @Override
    public RepairEvaluationVO getRepairEvaluationByOrderId(Long orderId) {
        QueryWrapper<RepairEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        RepairEvaluation repairEvaluation = this.getOne(queryWrapper);
        return getRepairEvaluationVO(repairEvaluation);
    }
}
