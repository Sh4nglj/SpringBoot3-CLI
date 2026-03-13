package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.RepairActionTypeEnum;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.PageUtils;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 维修记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    private final RepairOrderMapper repairOrderMapper;
    private final UserMapper userMapper;
    private final INotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRecord(RepairRecordAddRequest request, Long repairerId) {
        RepairOrder order = repairOrderMapper.selectById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 验证工单状态
        if (!RepairOrderStatusEnum.IN_PROGRESS.getValue().equals(order.getStatus()) &&
                !RepairOrderStatusEnum.ASSIGNED.getValue().equals(order.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不正确");
        }

        // 验证维修人员权限
        if (!repairerId.equals(order.getAssigneeId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是您负责的工单");
        }

        RepairRecord record = new RepairRecord();
        record.setOrderId(request.getOrderId());
        record.setRepairerId(repairerId);
        record.setActionType(request.getActionType());
        record.setContent(request.getContent());
        record.setCost(request.getCost());
        record.setSpareParts(request.getSpareParts());

        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            record.setPhotos(String.join(",", request.getPhotos()));
        }

        save(record);

        // 如果是开始维修操作，更新工单状态
        if (RepairActionTypeEnum.START_REPAIR.getValue().equals(request.getActionType())) {
            order.setStatus(RepairOrderStatusEnum.IN_PROGRESS.getValue());
            repairOrderMapper.updateById(order);
            
            // 发送通知给用户
            String title = "维修人员开始维修";
            String content = String.format("您的工单【%s】维修人员已开始维修。", order.getOrderNo());
            notificationService.createNotification(order.getUserId(), repairerId, title, content, NotificationTypeEnum.ORDER.getValue(), request.getOrderId());
        }

        return record.getId();
    }

    @Override
    public List<RepairRecordVO> getRecordsByOrderId(Long orderId) {
        LambdaQueryWrapper<RepairRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRecord::getOrderId, orderId)
                .orderByAsc(RepairRecord::getCreateTime);

        List<RepairRecord> records = list(wrapper);
        records = records != null ? records : Collections.emptyList();
        return records.stream()
                .map(this::convertToVO)
                .toList();
    }

    @Override
    public Page<RepairRecordVO> pageRecords(Long repairerId, int current, int pageSize) {
        Page<RepairRecord> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<RepairRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairRecord::getRepairerId, repairerId)
                .orderByDesc(RepairRecord::getCreateTime);

        Page<RepairRecord> recordPage = page(page, wrapper);
        return PageUtils.convert(recordPage, this::convertToVO);
    }

    @Override
    public RepairRecordVO convertToVO(RepairRecord record) {
        if (record == null) {
            return null;
        }
        RepairRecordVO vo = BeanUtil.copyProperties(record, RepairRecordVO.class);

        // 处理照片
        if (StrUtil.isNotBlank(record.getPhotos())) {
            vo.setPhotos(Arrays.asList(record.getPhotos().split(",")));
        }

        // 操作类型文本
        RepairActionTypeEnum actionType = RepairActionTypeEnum.getEnumByValue(record.getActionType());
        if (actionType != null) {
            vo.setActionTypeText(actionType.getText());
        }

        // 维修人员信息
        User repairer = userMapper.selectById(record.getRepairerId());
        if (repairer != null) {
            vo.setRepairerName(repairer.getUserName());
        }

        return vo;
    }
}
