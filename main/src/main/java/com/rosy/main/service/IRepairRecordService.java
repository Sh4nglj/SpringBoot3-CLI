package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.repair.RepairRecordVO;

import java.util.List;

/**
 * 维修记录服务接口
 */
public interface IRepairRecordService extends IService<RepairRecord> {

    /**
     * 添加维修记录
     */
    Long addRecord(RepairRecordAddRequest request, Long repairerId);

    /**
     * 根据工单ID查询维修记录列表
     */
    List<RepairRecordVO> getRecordsByOrderId(Long orderId);

    /**
     * 分页查询维修记录
     */
    Page<RepairRecordVO> pageRecords(Long repairerId, int current, int pageSize);

    /**
     * 转换为VO
     */
    RepairRecordVO convertToVO(RepairRecord record);
}
