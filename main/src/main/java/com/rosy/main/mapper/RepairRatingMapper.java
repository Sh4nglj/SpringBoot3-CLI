package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 评价Mapper
 */
@Mapper
public interface RepairRatingMapper extends BaseMapper<RepairRating> {

    /**
     * 计算维修人员平均评分
     */
    @Select("SELECT AVG(rating) FROM repair_rating WHERE repairer_id = #{repairerId} AND is_deleted = 0")
    Double calculateAverageRating(@Param("repairerId") Long repairerId);
}
