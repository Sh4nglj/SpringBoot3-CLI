package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    List<Map<String, Object>> countPendingOrdersByAssignee(@Param("statusList") List<Integer> statusList);

}
