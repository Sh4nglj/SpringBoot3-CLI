package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 维修记录表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface IRepairRecordService extends IService<RepairRecord> {

    /**
     * 校验数据
     *
     * @param repairRecord
     * @param add          是否为创建校验
     */
    void validRepairRecord(RepairRecord repairRecord, boolean add);

    /**
     * 获取维修记录VO
     *
     * @param repairRecord
     * @return
     */
    RepairRecordVO getRepairRecordVO(RepairRecord repairRecord);

    /**
     * 添加维修记录
     *
     * @param repairRecordAddRequest
     * @param request
     * @return
     */
    Long addRepairRecord(RepairRecordAddRequest repairRecordAddRequest, HttpServletRequest request);

    /**
     * 根据报修单ID获取维修记录
     *
     * @param orderId
     * @return
     */
    List<RepairRecordVO> getRepairRecordsByOrderId(Long orderId);
}
