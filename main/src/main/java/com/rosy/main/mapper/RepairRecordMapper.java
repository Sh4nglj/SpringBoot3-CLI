package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修记录Mapper
 */
@Mapper
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {
}
