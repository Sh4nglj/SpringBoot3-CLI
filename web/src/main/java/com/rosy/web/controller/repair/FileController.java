package com.rosy.web.controller.repair;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.web.service.IFileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件上传 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-26
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IFileService fileService;

    /**
     * 上传故障照片（单张）
     */
    @PostMapping("/upload/fault-image")
    public ApiResponse uploadFaultImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadFile(file, "fault-images");
        return ApiResponse.success(url);
    }

    /**
     * 上传故障照片（多张）
     */
    @PostMapping("/upload/fault-images")
    public ApiResponse uploadFaultImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> urls = fileService.uploadFiles(files, "fault-images");
        return ApiResponse.success(urls);
    }

    /**
     * 上传维修后照片（单张）
     */
    @PostMapping("/upload/repair-image")
    public ApiResponse uploadRepairImage(@RequestParam("file") MultipartFile file) {
        String url = fileService.uploadFile(file, "repair-images");
        return ApiResponse.success(url);
    }

    /**
     * 上传维修后照片（多张）
     */
    @PostMapping("/upload/repair-images")
    public ApiResponse uploadRepairImages(@RequestParam("files") List<MultipartFile> files) {
        List<String> urls = fileService.uploadFiles(files, "repair-images");
        return ApiResponse.success(urls);
    }

    /**
     * 通用文件上传
     *
     * @param file   文件
     * @param folder 文件夹名称
     */
    @PostMapping("/upload")
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "folder", defaultValue = "common") String folder) {
        String url = fileService.uploadFile(file, folder);
        return ApiResponse.success(url);
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    @PostMapping("/delete")
    public ApiResponse deleteFile(@RequestParam("fileUrl") String fileUrl) {
        boolean result = fileService.deleteFile(fileUrl);
        return ApiResponse.success(result);
    }
}
