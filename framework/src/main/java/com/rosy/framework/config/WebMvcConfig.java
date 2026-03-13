package com.rosy.framework.config;

import com.rosy.framework.config.properties.PathProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private PathProperties pathProperties;

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传文件的访问路径
        String uploadPath = pathProperties.getFileUploadPath();
        if (uploadPath == null || uploadPath.isBlank() || "xxx".equals(uploadPath)) {
            uploadPath = System.getProperty("user.dir") + "/uploads/";
        }
        if (!uploadPath.endsWith("/")) {
            uploadPath += "/";
        }

        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 映射 /uploads/** 到文件系统路径
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
