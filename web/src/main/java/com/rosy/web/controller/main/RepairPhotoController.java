package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.RepairPhoto;
import com.rosy.main.domain.vo.RepairPhotoVO;
import com.rosy.main.service.IRepairPhotoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 报修照片 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/repair/photo")
public class RepairPhotoController {
    @Resource
    IRepairPhotoService repairPhotoService;

    /**
     * 上传故障照片
     */
    @PostMapping("/upload")
    public ApiResponse uploadPhoto(@RequestParam Long orderId,
                                   @RequestParam("file") MultipartFile file) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件不能为空");
        }
        String photoUrl = repairPhotoService.uploadPhoto(orderId, file);
        return ApiResponse.success(photoUrl);
    }

    /**
     * 批量上传照片
     */
    @PostMapping("/upload/batch")
    public ApiResponse uploadPhotos(@RequestParam Long orderId,
                                    @RequestParam("files") MultipartFile[] files) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (files == null || files.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件不能为空");
        }
        List<String> photoUrls = repairPhotoService.uploadPhotos(orderId, files);
        return ApiResponse.success(photoUrls);
    }

    /**
     * 删除照片
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deletePhoto(@RequestBody IdRequest idRequest) {
        boolean result = repairPhotoService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 获取订单的照片列表
     */
    @GetMapping("/list")
    public ApiResponse getPhotosByOrderId(@RequestParam Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<RepairPhoto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairPhoto::getOrderId, orderId)
                .orderByAsc(RepairPhoto::getCreateTime);
        List<RepairPhoto> photos = repairPhotoService.list(wrapper);
        List<RepairPhotoVO> photoVOs = photos.stream()
                .map(repairPhotoService::getRepairPhotoVO)
                .collect(Collectors.toList());
        return ApiResponse.success(photoVOs);
    }

    /**
     * 根据ID获取照片详情
     */
    @GetMapping("/get")
    public ApiResponse getPhotoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairPhoto photo = repairPhotoService.getById(id);
        ThrowUtils.throwIf(photo == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(photo);
    }
}
