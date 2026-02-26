package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

public interface IRepairOrderService extends IService<RepairOrder> {

    Long addRepairOrder(RepairOrderAddRequest request);

    Boolean autoAssign(Long id);

    Boolean manualAssign(Long id, Long assigneeId);

    Boolean acceptOrder(Long id);

    Boolean completeOrder(Long id, String repairResult);

    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    Page<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> repairOrderPage);

    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request);
}
