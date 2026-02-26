package com.rosy.main.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String uploadFile(MultipartFile file, String basePath);

    String uploadImage(MultipartFile file);
}
