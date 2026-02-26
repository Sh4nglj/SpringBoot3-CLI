package com.rosy.web.controller.repair;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.service.IFileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/repair/file")
public class FileController {

    @Resource
    private IFileService fileService;

    @PostMapping("/upload/image")
    public ApiResponse uploadImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadImage(file);
        return ApiResponse.success(url);
    }

    @PostMapping("/upload/images")
    public ApiResponse uploadImages(@RequestParam("files") MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = fileService.uploadImage(file);
            urls.add(url);
        }
        return ApiResponse.success(urls);
    }
}
