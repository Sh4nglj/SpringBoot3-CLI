package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报修工单表 Mapper 接口
 *
 * @author Rosy
 */
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

}
