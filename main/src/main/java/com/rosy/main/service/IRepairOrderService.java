package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

import java.math.BigDecimal;
import java.util.List;

public interface IRepairOrderService extends IService<RepairOrder> {

    String generateOrderNo();

    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest queryRequest);

    void assignOrder(RepairOrderAssignRequest assignRequest, boolean isAuto);

    void autoAssignOrder(Long orderId);

    void acceptOrder(Long orderId, Long repairerId);

    void completeOrderWithResult(Long orderId, String repairResult, List<String> repairImages, BigDecimal repairCost);

    void cancelOrder(Long orderId);
}
