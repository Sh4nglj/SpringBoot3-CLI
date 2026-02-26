package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Repairman;
import com.rosy.main.domain.vo.RepairmanVO;
import com.rosy.main.enums.RepairmanWorkStatusEnum;
import com.rosy.main.mapper.RepairmanMapper;
import com.rosy.main.service.IRepairmanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修人员表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Service
public class RepairmanServiceImpl extends ServiceImpl<RepairmanMapper, Repairman> implements IRepairmanService {

    @Override
    public RepairmanVO getRepairmanVO(Repairman repairman) {
        if (repairman == null) {
            return null;
        }
        RepairmanVO repairmanVO = new RepairmanVO();
        BeanUtils.copyProperties(repairman, repairmanVO);

        // 设置工作状态文本
        repairmanVO.setWorkStatusText(RepairmanWorkStatusEnum.getTextByValue(repairman.getWorkStatus()));
        repairmanVO.setStatusText(repairman.getStatus() == 1 ? "启用" : "禁用");

        return repairmanVO;
    }

    @Override
    public List<RepairmanVO> getAvailableRepairmen() {
        QueryWrapper<Repairman> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_status", RepairmanWorkStatusEnum.ONLINE_IDLE.getValue());
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("current_orders");
        queryWrapper.orderByDesc("average_rating");

        List<Repairman> repairmanList = this.list(queryWrapper);
        return repairmanList.stream()
                .map(this::getRepairmanVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RepairmanVO> getAvailableRepairmenBySkill(String skill) {
        List<Repairman> repairmanList;
        if (skill != null && !skill.isEmpty()) {
            repairmanList = baseMapper.selectAvailableRepairmenBySkill(skill, 10);
        } else {
            repairmanList = baseMapper.selectAvailableRepairmen(10);
        }
        return repairmanList.stream()
                .map(this::getRepairmanVO)
                .collect(Collectors.toList());
    }

    @Override
    public Repairman autoAssignRepairman(String deviceType) {
        // 根据设备类型尝试匹配有相关技能的维修人员
        List<Repairman> repairmanList = baseMapper.selectAvailableRepairmenBySkill(deviceType, 1);

        // 如果没有匹配的，获取任意空闲维修人员
        if (repairmanList == null || repairmanList.isEmpty()) {
            repairmanList = baseMapper.selectAvailableRepairmen(1);
        }

        if (repairmanList == null || repairmanList.isEmpty()) {
            return null;
        }

        return repairmanList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementCurrentOrders(Long repairmanId) {
        Repairman repairman = this.getById(repairmanId);
        if (repairman == null) {
            return;
        }

        repairman.setCurrentOrders(repairman.getCurrentOrders() + 1);

        // 如果当前工单数超过阈值，设置为忙碌状态
        if (repairman.getCurrentOrders() >= 3) {
            repairman.setWorkStatus(RepairmanWorkStatusEnum.BUSY.getValue());
        }

        this.updateById(repairman);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decrementCurrentOrders(Long repairmanId) {
        Repairman repairman = this.getById(repairmanId);
        if (repairman == null) {
            return;
        }

        repairman.setCurrentOrders(Math.max(0, repairman.getCurrentOrders() - 1));

        // 如果当前工单数低于阈值，设置为空闲状态
        if (repairman.getCurrentOrders() < 3) {
            repairman.setWorkStatus(RepairmanWorkStatusEnum.ONLINE_IDLE.getValue());
        }

        this.updateById(repairman);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAverageRating(Long repairmanId, Integer newRating) {
        Repairman repairman = this.getById(repairmanId);
        if (repairman == null) {
            return;
        }

        // 计算新的平均评分
        Double currentAvg = repairman.getAverageRating();
        Integer totalCompleted = repairman.getTotalCompletedOrders();

        if (currentAvg == null || totalCompleted == null || totalCompleted == 0) {
            repairman.setAverageRating((double) newRating);
        } else {
            double newAvg = (currentAvg * totalCompleted + newRating) / (totalCompleted + 1);
            repairman.setAverageRating(Math.round(newAvg * 100.0) / 100.0);
        }

        repairman.setTotalCompletedOrders(totalCompleted + 1);
        this.updateById(repairman);
    }
}
