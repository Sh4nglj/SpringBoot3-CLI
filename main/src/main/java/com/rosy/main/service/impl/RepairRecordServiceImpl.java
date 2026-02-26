package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.enums.RepairResultEnum;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修记录表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public void validRepairRecord(RepairRecord repairRecord, boolean add) {
        if (repairRecord == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long orderId = repairRecord.getOrderId();
        String repairContent = repairRecord.getRepairContent();

        // 创建时，参数不能为空
        if (add) {
            if (orderId == null || orderId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "报修单ID不能为空");
            }
            if (StrUtil.isBlank(repairContent)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "维修内容不能为空");
            }
        }

        // 有参数则校验
        if (StrUtil.isNotBlank(repairContent) && repairContent.length() > 2000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "维修内容过长");
        }
        if (StrUtil.isNotBlank(repairRecord.getReplacedParts()) && repairRecord.getReplacedParts().length() > 1000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更换配件信息过长");
        }
        if (StrUtil.isNotBlank(repairRecord.getRemark()) && repairRecord.getRemark().length() > 1000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "备注过长");
        }
    }

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord repairRecord) {
        if (repairRecord == null) {
            return null;
        }
        RepairRecordVO repairRecordVO = new RepairRecordVO();
        BeanUtils.copyProperties(repairRecord, repairRecordVO);

        // 设置维修结果文本
        repairRecordVO.setRepairResultText(RepairResultEnum.getTextByValue(repairRecord.getRepairResult()));

        // 处理图片列表
        if (StrUtil.isNotBlank(repairRecord.getRepairImages())) {
            repairRecordVO.setRepairImages(List.of(repairRecord.getRepairImages().split(",")));
        }

        return repairRecordVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRepairRecord(RepairRecordAddRequest repairRecordAddRequest, HttpServletRequest request) {
        Long orderId = repairRecordAddRequest.getOrderId();

        // 检查报修单是否存在
        RepairOrder repairOrder = repairOrderService.getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        // 检查报修单状态
        if (!repairOrder.getStatus().equals(RepairOrderStatusEnum.REPAIRING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报修单不在维修中状态");
        }

        // TODO: 从request中获取当前登录的维修人员ID，验证是否为分配的维修人员
        Long currentRepairmanId = 1L;
        if (!repairOrder.getRepairmanId().equals(currentRepairmanId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您不是该报修单的分配维修人员");
        }

        RepairRecord repairRecord = new RepairRecord();
        BeanUtils.copyProperties(repairRecordAddRequest, repairRecord);
        repairRecord.setRepairmanId(currentRepairmanId);

        // 处理图片
        if (repairRecordAddRequest.getRepairImages() != null && !repairRecordAddRequest.getRepairImages().isEmpty()) {
            repairRecord.setRepairImages(String.join(",", repairRecordAddRequest.getRepairImages()));
        }

        validRepairRecord(repairRecord, true);
        boolean result = this.save(repairRecord);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加维修记录失败");
        }

        return repairRecord.getId();
    }

    @Override
    public List<RepairRecordVO> getRepairRecordsByOrderId(Long orderId) {
        QueryWrapper<RepairRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.orderByAsc("create_time");

        List<RepairRecord> repairRecordList = this.list(queryWrapper);
        return repairRecordList.stream()
                .map(this::getRepairRecordVO)
                .collect(Collectors.toList());
    }
}
