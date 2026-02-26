package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Repairman;
import com.rosy.main.domain.vo.RepairmanVO;

import java.util.List;

/**
 * <p>
 * 维修人员表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface IRepairmanService extends IService<Repairman> {

    /**
     * 获取维修人员VO
     *
     * @param repairman
     * @return
     */
    RepairmanVO getRepairmanVO(Repairman repairman);

    /**
     * 获取所有可用的维修人员
     *
     * @return
     */
    List<RepairmanVO> getAvailableRepairmen();

    /**
     * 根据技能获取可用的维修人员
     *
     * @param skill
     * @return
     */
    List<RepairmanVO> getAvailableRepairmenBySkill(String skill);

    /**
     * 自动分配最佳维修人员
     *
     * @param deviceType
     * @return
     */
    Repairman autoAssignRepairman(String deviceType);

    /**
     * 增加维修人员当前工单数
     *
     * @param repairmanId
     */
    void incrementCurrentOrders(Long repairmanId);

    /**
     * 减少维修人员当前工单数
     *
     * @param repairmanId
     */
    void decrementCurrentOrders(Long repairmanId);

    /**
     * 更新维修人员平均评分
     *
     * @param repairmanId
     * @param newRating
     */
    void updateAverageRating(Long repairmanId, Integer newRating);
}
