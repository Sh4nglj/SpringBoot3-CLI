package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 报修单Mapper
 */
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    /**
     * 根据维修人员ID统计待处理工单数量
     */
    @Select("SELECT COUNT(*) FROM repair_order WHERE assignee_id = #{repairerId} AND status IN (1, 2) AND is_deleted = 0")
    Integer countPendingByRepairerId(@Param("repairerId") Long repairerId);

    /**
     * 根据设备类型查找可分配的维修人员（支持自动分配）
     */
    @Select("SELECT DISTINCT rs.user_id FROM repairer_skill rs " +
            "INNER JOIN user u ON rs.user_id = u.id " +
            "WHERE rs.device_type = #{deviceType} AND u.user_role = 'repair' AND u.is_deleted = 0 " +
            "ORDER BY rs.proficiency DESC")
    List<Long> findRepairerIdsByDeviceType(@Param("deviceType") String deviceType);
}
