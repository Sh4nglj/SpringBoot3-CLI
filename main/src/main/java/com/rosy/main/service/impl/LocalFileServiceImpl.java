package com.rosy.main.service.impl;

import cn.hutool.core.util.IdUtil;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class LocalFileServiceImpl implements IFileService {

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:http://localhost:8080/uploads}")
    private String urlPrefix;

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过10MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不正确");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的图片格式，仅支持jpg、jpeg、png、gif、webp");
        }

        String fileName = IdUtil.simpleUUID() + "." + extension;
        String subDir = fileName.substring(0, 2) + File.separator + fileName.substring(2, 4);
        File dir = new File(uploadPath + File.separator + subDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建目录失败");
        }

        File destFile = new File(dir, fileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存文件失败");
        }

        return urlPrefix + "/" + subDir.replace(File.separator, "/") + "/" + fileName;
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith(urlPrefix)) {
            return;
        }
        String relativePath = fileUrl.substring(urlPrefix.length() + 1);
        File file = new File(uploadPath + File.separator + relativePath);
        if (file.exists()) {
            file.delete();
        }
    }
}