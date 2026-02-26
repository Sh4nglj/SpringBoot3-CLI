package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

public interface IRepairOrderService extends IService<RepairOrder> {

    String generateOrderNo();

    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest queryRequest);

    void assignOrder(RepairOrderAssignRequest assignRequest, boolean isAuto);

    void acceptOrder(Long orderId, Long repairerId);

    void completeOrder(Long orderId);

    void cancelOrder(Long orderId);
}
