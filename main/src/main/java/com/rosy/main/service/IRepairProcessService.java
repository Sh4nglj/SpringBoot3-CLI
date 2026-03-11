package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairProcess;

import java.util.List;

/**
 * 维修过程记录表 服务类
 *
 * @author Rosy
 */
public interface IRepairProcessService extends IService<RepairProcess> {

    /**
     * 添加维修过程记录
     *
     * @param orderId     工单ID
     * @param operatorId  操作人ID
     * @param operatorName 操作人名称
     * @param actionType  操作类型
     * @param content     操作内容
     * @return 是否成功
     */
    Boolean addProcess(Long orderId, Long operatorId, String operatorName, Byte actionType, String content);

    /**
     * 根据工单ID获取维修过程记录
     *
     * @param orderId 工单ID
     * @return 维修过程记录列表
     */
    List<RepairProcess> getProcessByOrderId(Long orderId);
}
