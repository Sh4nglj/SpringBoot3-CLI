package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.repair.RepairOrderVO;

/**
 * 报修单服务接口
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    /**
     * 提交报修申请
     */
    Long submitRepairOrder(RepairOrderSubmitRequest request);

    /**
     * 自动分配工单
     */
    boolean autoAssignOrder(Long orderId);

    /**
     * 手动分配工单
     */
    boolean manualAssignOrder(RepairOrderAssignRequest request);

    /**
     * 维修人员接单
     */
    boolean acceptOrder(Long orderId, Long repairerId);

    /**
     * 完成维修
     */
    boolean completeRepair(CompleteRepairRequest request, Long repairerId);

    /**
     * 用户确认完成
     */
    boolean confirmComplete(Long orderId, Long userId);

    /**
     * 取消工单
     */
    boolean cancelOrder(Long orderId, Long userId);

    /**
     * 设置优先级
     */
    boolean setPriority(Long orderId, Byte priority);

    /**
     * 获取报修单详情
     */
    RepairOrderVO getOrderDetail(Long id);

    /**
     * 分页查询报修单
     */
    Page<RepairOrderVO> pageOrder(RepairOrderQueryRequest request);

    /**
     * 查询用户的报修单
     */
    Page<RepairOrderVO> pageUserOrder(Long userId, RepairOrderQueryRequest request);

    /**
     * 查询维修人员的工单
     */
    Page<RepairOrderVO> pageRepairerOrder(Long repairerId, RepairOrderQueryRequest request);

    /**
     * 构建查询条件
     */
    LambdaQueryWrapper<RepairOrder> buildQueryWrapper(RepairOrderQueryRequest request);

    /**
     * 生成报修单号
     */
    String generateOrderNo();
}
