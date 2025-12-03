package com.example.demo.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.demo.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * OSS文件上传服务类
 * 提供文件上传到阿里云OSS的核心功能
 */
@Service
public class OssService {

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    /**
     * 允许上传的图片格式
     */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp");

    /**
     * 最大文件大小：10MB
     */
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 上传图片到OSS
     * 
     * @param file 要上传的文件
     * @return 文件在OSS中的存储路径（key）
     * @throws IOException              文件读取异常
     * @throws IllegalArgumentException 文件验证失败
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 验证文件
        validateFile(file);

        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        // 构建OSS文件路径（可以按日期分目录）
        String ossFilePath = "images/" + fileName;

        // 上传文件到OSS
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    ossFilePath,
                    inputStream);

            ossClient.putObject(putObjectRequest);
        }

        return ossFilePath;
    }

    /**
     * 根据OSS文件路径生成访问URL
     * 
     * @param ossFilePath OSS文件路径
     * @return 完整的访问URL
     */
    public String getFileUrl(String ossFilePath) {
        return ossConfig.getUrlPrefix() + ossFilePath;
    }

    /**
     * 验证上传的文件
     * 
     * @param file 要验证的文件
     * @throws IllegalArgumentException 验证失败时抛出异常
     */
    private void validateFile(MultipartFile file) {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("只支持上传图片格式：JPG, PNG, GIF, BMP, WEBP");
        }
    }

    /**
     * 生成唯一的文件名
     * 
     * @param originalFilename 原始文件名
     * @return 唯一的文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        // 获取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 使用UUID生成唯一文件名
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }
}
