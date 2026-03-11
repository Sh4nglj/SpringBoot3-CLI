package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairProcess;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修过程记录表 Mapper 接口
 *
 * @author Rosy
 */
@Mapper
public interface RepairProcessMapper extends BaseMapper<RepairProcess> {

}
