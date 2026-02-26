package com.rosy.web.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.framework.config.properties.PathProperties;
import com.rosy.web.service.IFileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件服务实现类
 */
@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    @Resource
    private PathProperties pathProperties;

    // 允许上传的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
    );

    // 最大文件大小 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        // 校验文件
        validateFile(file);

        // 生成文件路径
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.extName(originalFilename);
        String newFileName = IdUtil.fastSimpleUUID() + "." + suffix;

        // 按日期分文件夹
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = folder + "/" + dateFolder + "/" + newFileName;
        String fullPath = pathProperties.getFileUploadPath() + "/" + relativePath;

        // 创建目录
        FileUtil.mkParentDirs(fullPath);

        // 保存文件
        try {
            File destFile = new File(fullPath);
            file.transferTo(destFile);
            log.info("文件上传成功: {}", fullPath);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }

        // 返回可访问的URL
        return getFullUrl(relativePath);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folder) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String url = uploadFile(file, folder);
                urls.add(url);
            }
        }
        return urls;
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return false;
        }

        try {
            // 从URL中提取相对路径
            String relativePath = extractRelativePath(fileUrl);
            if (StrUtil.isBlank(relativePath)) {
                return false;
            }

            String fullPath = pathProperties.getFileUploadPath() + "/" + relativePath;
            boolean result = FileUtil.del(fullPath);
            if (result) {
                log.info("文件删除成功: {}", fullPath);
            }
            return result;
        } catch (Exception e) {
            log.error("文件删除失败: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public String getFullUrl(String relativePath) {
        // 这里可以根据实际部署情况配置域名或IP
        // 默认使用相对路径，前端可通过配置的基础URL拼接
        return "/uploads/" + relativePath;
    }

    /**
     * 校验文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }

        // 校验文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过10MB");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只支持上传图片文件(jpg/png/gif/webp/bmp)");
        }
    }

    /**
     * 从URL中提取相对路径
     */
    private String extractRelativePath(String fileUrl) {
        // 移除基础路径前缀，获取相对路径
        if (fileUrl.startsWith("/uploads/")) {
            return fileUrl.substring("/uploads/".length());
        }
        return fileUrl;
    }
}
