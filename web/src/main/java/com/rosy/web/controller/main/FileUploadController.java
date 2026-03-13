package com.rosy.web.controller.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.framework.config.properties.PathProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileUploadController {

    @Resource
    private PathProperties pathProperties;

    /**
     * 支持的图片类型
     */
    private static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};

    /**
     * 最大文件大小 10MB
     */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 单文件上传
     */
    @PostMapping("/upload")
    @Operation(summary = "单文件上传")
    public ApiResponse uploadFile(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择要上传的文件");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过10MB");
        }

        // 获取文件名和后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);

        // 检查文件类型
        if (!isAllowedImageType(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的文件类型");
        }

        // 生成新的文件名
        String newFileName = IdUtil.simpleUUID() + "." + suffix;

        // 获取上传路径
        String uploadPath = getUploadPath();

        // 创建目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        File destFile = new File(uploadPath + newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }

        // 返回文件访问URL
        String fileUrl = getFileUrl(request, newFileName);
        return ApiResponse.success(fileUrl);
    }

    /**
     * 多文件上传
     */
    @PostMapping("/upload/batch")
    @Operation(summary = "多文件上传")
    public ApiResponse uploadFiles(MultipartFile[] files, HttpServletRequest request) {
        if (files == null || files.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择要上传的文件");
        }

        if (files.length > 9) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多支持上传9个文件");
        }

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // 检查文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过10MB");
            }

            // 获取文件名和后缀
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);

            // 检查文件类型
            if (!isAllowedImageType(suffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的文件类型：" + originalFilename);
            }

            // 生成新的文件名
            String newFileName = IdUtil.simpleUUID() + "." + suffix;

            // 获取上传路径
            String uploadPath = getUploadPath();

            // 创建目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            File destFile = new File(uploadPath + newFileName);
            try {
                file.transferTo(destFile);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败：" + originalFilename);
            }

            // 返回文件访问URL
            String fileUrl = getFileUrl(request, newFileName);
            fileUrls.add(fileUrl);
        }

        return ApiResponse.success(fileUrls);
    }

    /**
     * 检查是否为允许的图片类型
     */
    private boolean isAllowedImageType(String suffix) {
        if (StrUtil.isBlank(suffix)) {
            return false;
        }
        String lowerSuffix = suffix.toLowerCase();
        for (String type : ALLOWED_IMAGE_TYPES) {
            if (type.equals(lowerSuffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取上传路径
     */
    private String getUploadPath() {
        String uploadPath = pathProperties.getFileUploadPath();
        if (StrUtil.isBlank(uploadPath) || "xxx".equals(uploadPath)) {
            // 默认路径
            uploadPath = System.getProperty("user.dir") + "/uploads/";
        }
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }
        return uploadPath;
    }

    /**
     * 获取文件访问URL
     */
    private String getFileUrl(HttpServletRequest request, String fileName) {
        // 构建访问URL
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return baseUrl + "/uploads/" + fileName;
    }
}
