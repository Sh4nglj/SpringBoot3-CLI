package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.AssignmentMethodEnum;
import com.rosy.common.enums.DeviceTypeEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.FaultTypeEnum;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.enums.RepairPriorityEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Resource
    private INotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRepairOrder(RepairOrderAddRequest request) {
        RepairOrder repairOrder = BeanUtil.copyProperties(request, RepairOrder.class);
        repairOrder.setOrderNo(generateOrderNo());
        repairOrder.setStatus(RepairOrderStatusEnum.PENDING.getValue());
        if (repairOrder.getPriority() == null) {
            repairOrder.setPriority(RepairPriorityEnum.MEDIUM.getValue());
        }
        boolean result = this.save(repairOrder);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建工单失败");
        }
        return repairOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean autoAssign(Long id) {
        RepairOrder repairOrder = this.getById(id);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.PENDING.getValue().equals(repairOrder.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有待处理状态才能分配");
        }
        Long assigneeId = findAvailableAssignee();
        if (assigneeId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "没有可用的维修人员");
        }
        repairOrder.setAssigneeId(assigneeId);
        repairOrder.setAssignmentMethod(AssignmentMethodEnum.AUTO.getValue());
        repairOrder.setAssignedTime(new Date());
        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean manualAssign(Long id, Long assigneeId) {
        RepairOrder repairOrder = this.getById(id);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.PENDING.getValue().equals(repairOrder.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有待处理状态才能分配");
        }
        repairOrder.setAssigneeId(assigneeId);
        repairOrder.setAssignmentMethod(AssignmentMethodEnum.MANUAL.getValue());
        repairOrder.setAssignedTime(new Date());
        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean acceptOrder(Long id) {
        RepairOrder repairOrder = this.getById(id);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.PENDING.getValue().equals(repairOrder.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有待处理状态才能接单");
        }
        if (repairOrder.getAssigneeId() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先分配工单");
        }
        repairOrder.setStatus(RepairOrderStatusEnum.IN_PROGRESS.getValue());
        repairOrder.setStartedTime(new Date());
        return this.updateById(repairOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeOrder(Long id, String repairResult) {
        RepairOrder repairOrder = this.getById(id);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!RepairOrderStatusEnum.IN_PROGRESS.getValue().equals(repairOrder.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有维修中状态才能完成");
        }
        repairOrder.setStatus(RepairOrderStatusEnum.COMPLETED.getValue());
        repairOrder.setCompletedTime(new Date());
        repairOrder.setRepairResult(repairResult);
        boolean result = this.updateById(repairOrder);
        if (result && repairOrder.getCreateBy() != null) {
            notificationService.sendRepairCompletedNotification(repairOrder.getCreateBy(), id, repairResult);
        }
        return result;
    }

    @Override
    public RepairOrderVO getRepairOrderVO(RepairOrder repairOrder) {
        return Optional.ofNullable(repairOrder)
                .map(order -> {
                    RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
                    DeviceTypeEnum deviceType = DeviceTypeEnum.getEnumByValue(order.getDeviceType());
                    if (deviceType != null) {
                        vo.setDeviceTypeName(deviceType.getText());
                    }
                    FaultTypeEnum faultType = FaultTypeEnum.getEnumByValue(order.getFaultType());
                    if (faultType != null) {
                        vo.setFaultTypeName(faultType.getText());
                    }
                    RepairOrderStatusEnum status = RepairOrderStatusEnum.getEnumByValue(order.getStatus());
                    if (status != null) {
                        vo.setStatusName(status.getText());
                    }
                    RepairPriorityEnum priority = RepairPriorityEnum.getEnumByValue(order.getPriority());
                    if (priority != null) {
                        vo.setPriorityName(priority.getText());
                    }
                    AssignmentMethodEnum method = AssignmentMethodEnum.getEnumByValue(order.getAssignmentMethod());
                    if (method != null) {
                        vo.setAssignmentMethodName(method.getText());
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Page<RepairOrderVO> getRepairOrderVOPage(Page<RepairOrder> repairOrderPage) {
        Page<RepairOrderVO> voPage = new Page<>(repairOrderPage.getCurrent(), repairOrderPage.getSize(), repairOrderPage.getTotal());
        List<RepairOrderVO> voList = Optional.ofNullable(repairOrderPage.getRecords())
                .orElse(new ArrayList<>())
                .stream()
                .map(this::getRepairOrderVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();
        QueryWrapperUtil.addCondition(queryWrapper, request.getId(), RepairOrder::getId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getOrderNo(), RepairOrder::getOrderNo);
        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceType(), RepairOrder::getDeviceType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceLocation(), RepairOrder::getDeviceLocation);
        QueryWrapperUtil.addCondition(queryWrapper, request.getFaultType(), RepairOrder::getFaultType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), RepairOrder::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, request.getPriority(), RepairOrder::getPriority);
        QueryWrapperUtil.addCondition(queryWrapper, request.getCreateBy(), RepairOrder::getCreateBy);
        QueryWrapperUtil.addCondition(queryWrapper, request.getAssigneeId(), RepairOrder::getAssigneeId);
        QueryWrapperUtil.addSortCondition(queryWrapper,
                request.getSortField(),
                request.getSortOrder(),
                RepairOrder::getId);
        return queryWrapper;
    }

    private String generateOrderNo() {
        return "RO" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
    }

    private Long findAvailableAssignee() {
        List<Integer> pendingStatusList = Arrays.asList(
            RepairOrderStatusEnum.PENDING.getValue(),
            RepairOrderStatusEnum.IN_PROGRESS.getValue()
        );
        List<Map<String, Object>> assigneeWorkload = baseMapper.countPendingOrdersByAssignee(pendingStatusList);
        
        if (assigneeWorkload == null || assigneeWorkload.isEmpty()) {
            return getDefaultAssignee();
        }
        
        Object assigneeIdObj = assigneeWorkload.get(0).get("assigneeId");
        if (assigneeIdObj == null) {
            return getDefaultAssignee();
        }
        if (assigneeIdObj instanceof Long) {
            return (Long) assigneeIdObj;
        } else if (assigneeIdObj instanceof Integer) {
            return ((Integer) assigneeIdObj).longValue();
        } else if (assigneeIdObj instanceof BigDecimal) {
            return ((BigDecimal) assigneeIdObj).longValue();
        }
        return Long.valueOf(assigneeIdObj.toString());
    }

    private Long getDefaultAssignee() {
        return 1L;
    }
}
