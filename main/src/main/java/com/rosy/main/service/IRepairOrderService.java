package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.*;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 报修单表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface IRepairOrderService extends IService<RepairOrder> {

    /**
     * 校验数据
     *
     * @param repairOrder
     * @param add         是否为创建校验
     */
    void validRepairOrder(RepairOrder repairOrder, boolean add);

    /**
     * 获取查询条件
     *
     * @param repairOrderQueryRequest
     * @return
     */
    QueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest repairOrderQueryRequest);

    /**
     * 获取报修单VO
     *
     * @param repairOrder
     * @return
     */
    RepairOrderVO getRepairOrderVO(RepairOrder repairOrder);

    /**
     * 分页获取报修单VO
     *
     * @param repairOrderPage
     * @return
     */
    Page<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> repairOrderPage);

    /**
     * 创建报修单
     *
     * @param repairOrderAddRequest
     * @param request
     * @return
     */
    Long addRepairOrder(RepairOrderAddRequest repairOrderAddRequest, HttpServletRequest request);

    /**
     * 更新报修单
     *
     * @param repairOrderUpdateRequest
     * @param request
     * @return
     */
    Boolean updateRepairOrder(RepairOrderUpdateRequest repairOrderUpdateRequest, HttpServletRequest request);

    /**
     * 分配报修单
     *
     * @param repairOrderAssignRequest
     * @param request
     * @return
     */
    Boolean assignRepairOrder(RepairOrderAssignRequest repairOrderAssignRequest, HttpServletRequest request);

    /**
     * 自动分配报修单
     *
     * @param orderId
     * @return
     */
    Boolean autoAssignRepairOrder(Long orderId);

    /**
     * 维修人员接单
     *
     * @param repairOrderAcceptRequest
     * @param request
     * @return
     */
    Boolean acceptRepairOrder(RepairOrderAcceptRequest repairOrderAcceptRequest, HttpServletRequest request);

    /**
     * 完成报修单
     *
     * @param orderId
     * @param request
     * @return
     */
    Boolean completeRepairOrder(Long orderId, HttpServletRequest request);

    /**
     * 取消报修单
     *
     * @param orderId
     * @param request
     * @return
     */
    Boolean cancelRepairOrder(Long orderId, HttpServletRequest request);
}
