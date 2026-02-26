package com.rosy.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务接口
 */
public interface IFileService {

    /**
     * 上传单个文件
     *
     * @param file   文件
     * @param folder 文件夹名称
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * 上传多个文件
     *
     * @param files  文件列表
     * @param folder 文件夹名称
     * @return 文件访问URL列表
     */
    List<String> uploadFiles(List<MultipartFile> files, String folder);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否成功
     */
    boolean deleteFile(String fileUrl);

    /**
     * 获取文件的完整访问URL
     *
     * @param relativePath 相对路径
     * @return 完整URL
     */
    String getFullUrl(String relativePath);
}
