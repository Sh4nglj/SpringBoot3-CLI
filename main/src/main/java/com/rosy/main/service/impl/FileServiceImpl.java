package com.rosy.main.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.framework.config.properties.PathProperties;
import com.rosy.main.service.IFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements IFileService {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final String IMAGE_BASE_PATH = "repair/images";

    private final PathProperties pathProperties;

    public FileServiceImpl(PathProperties pathProperties) {
        this.pathProperties = pathProperties;
    }

    @Override
    public String uploadFile(MultipartFile file, String basePath) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过10MB");
        }
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名不能为空");
        }
        String extension = FileUtil.extName(originalFilename);
        String newFilename = IdUtil.fastSimpleUUID() + "." + extension;
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = basePath + "/" + datePath + "/" + newFilename;
        String uploadPath = pathProperties.getFileUploadPath();
        if (StrUtil.isBlank(uploadPath)) {
            uploadPath = System.getProperty("user.dir") + "/uploads";
        }
        File destFile = new File(uploadPath, relativePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        }
        return "/uploads/" + relativePath;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "仅支持JPEG、PNG、GIF、WEBP格式的图片");
        }
        return uploadFile(file, IMAGE_BASE_PATH);
    }
}
