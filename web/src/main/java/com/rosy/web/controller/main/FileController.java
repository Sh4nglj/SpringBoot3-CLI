package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IFileService fileService;

    @Operation(summary = "上传图片")
    @PostMapping("/upload/image")
    public ApiResponse uploadImage(@Parameter(description = "图片文件") @RequestPart("file") MultipartFile file) {
        String url = fileService.uploadImage(file);
        return ApiResponse.success(url);
    }
}