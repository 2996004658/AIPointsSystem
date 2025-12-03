package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件上传响应DTO
 * 返回给前端的上传结果信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    /**
     * 上传记录ID
     */
    private Long id;

    /**
     * 文件访问URL
     */
    private String url;

    /**
     * 文件原始名称
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 简化构造函数，只包含URL和文件名
     * 
     * @param url      文件URL
     * @param fileName 文件名
     */
    public UploadResponse(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }
}
