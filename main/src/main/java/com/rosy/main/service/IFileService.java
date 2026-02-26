package com.rosy.main.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String uploadImage(MultipartFile file);

    void deleteFile(String fileUrl);
}