package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;

import java.util.List;

/**
 * 报修工单表 服务类
 *
 * @author Rosy
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    /**
     * 创建报修工单
     *
     * @param request 创建请求
     * @return 工单ID
     */
    Long createOrder(RepairOrderCreateRequest request);

    /**
     * 分配工单
     *
     * @param request 分配请求
     * @return 是否成功
     */
    Boolean assignOrder(RepairOrderAssignRequest request);

    /**
     * 自动分配工单
     *
     * @param orderId 工单ID
     * @return 是否成功
     */
    Boolean autoAssignOrder(Long orderId);

    /**
     * 维修人员接单
     *
     * @param orderId 工单ID
     * @return 是否成功
     */
    Boolean acceptOrder(Long orderId);

    /**
     * 开始维修
     *
     * @param orderId 工单ID
     * @return 是否成功
     */
    Boolean startRepair(Long orderId);

    /**
     * 完成维修
     *
     * @param request 完成请求
     * @return 是否成功
     */
    Boolean completeRepair(RepairCompleteRequest request);

    /**
     * 用户评价
     *
     * @param request 评价请求
     * @return 是否成功
     */
    Boolean rateOrder(RepairRatingRequest request);

    /**
     * 取消工单
     *
     * @param orderId 工单ID
     * @return 是否成功
     */
    Boolean cancelOrder(Long orderId);

    /**
     * 获取工单详情
     *
     * @param orderId 工单ID
     * @return 工单VO
     */
    RepairOrderVO getOrderDetail(Long orderId);

    /**
     * 获取用户的工单列表
     *
     * @param userId 用户ID
     * @return 工单列表
     */
    List<RepairOrderVO> getUserOrders(Long userId);

    /**
     * 分页获取用户的工单列表
     *
     * @param userId 用户ID
     * @param current 当前页
     * @param size 每页大小
     * @return 工单分页列表
     */
    Page<RepairOrder> getUserOrders(Long userId, long current, long size);

    /**
     * 获取维修人员的工单列表
     *
     * @param workerId 维修人员ID
     * @return 工单列表
     */
    List<RepairOrderVO> getWorkerOrders(Long workerId);

    /**
     * 分页获取维修人员的工单列表
     *
     * @param workerId 维修人员ID
     * @param current 当前页
     * @param size 每页大小
     * @return 工单分页列表
     */
    Page<RepairOrder> getWorkerOrders(Long workerId, long current, long size);

    /**
     * 更新工单状态
     *
     * @param orderId 工单ID
     * @param status  新状态
     * @return 是否成功
     */
    Boolean updateStatus(Long orderId, Byte status);

    /**
     * 获取查询条件
     *
     * @param request 查询请求
     * @return 查询条件
     */
    LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request);

    /**
     * 获取工单VO
     *
     * @param order 工单实体
     * @return 工单VO
     */
    RepairOrderVO getRepairOrderVO(RepairOrder order);
}
