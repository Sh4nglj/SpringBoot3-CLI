package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Repairman;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 维修人员表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
public interface RepairmanMapper extends BaseMapper<Repairman> {

    /**
     * 查询空闲的维修人员，按当前工单数升序排序
     */
    @Select("SELECT * FROM repairman WHERE work_status = 1 AND status = 1 AND is_deleted = 0 ORDER BY current_orders ASC, average_rating DESC LIMIT #{limit}")
    List<Repairman> selectAvailableRepairmen(@Param("limit") Integer limit);

    /**
     * 根据技能查询维修人员
     */
    @Select("SELECT * FROM repairman WHERE work_status = 1 AND status = 1 AND is_deleted = 0 AND skills LIKE CONCAT('%', #{skill}, '%') ORDER BY current_orders ASC, average_rating DESC LIMIT #{limit}")
    List<Repairman> selectAvailableRepairmenBySkill(@Param("skill") String skill, @Param("limit") Integer limit);
}
