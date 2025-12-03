package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 上传记录实体类
 * 用于记录文件上传到OSS的详细信息
 */
@Entity
@Table(name = "upload_records")
@Data
public class UploadRecord {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件原始名称
     */
    @Column(nullable = false)
    private String originalFileName;

    /**
     * 文件在OSS中的存储路径（key）
     */
    @Column(nullable = false)
    private String ossFilePath;

    /**
     * 文件访问URL
     */
    @Column(nullable = false, length = 500)
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @Column(nullable = false)
    private Long fileSize;

    /**
     * 文件MIME类型
     */
    @Column(nullable = false)
    private String contentType;

    /**
     * 上传时间
     */
    @Column(nullable = false)
    private LocalDateTime uploadTime;

    /**
     * 上传用户ID（可选，如果需要关联用户）
     */
    private Long userId;

    /**
     * 上传用户名（可选）
     */
    private String username;

    /**
     * 在持久化之前自动设置上传时间
     */
    @PrePersist
    protected void onCreate() {
        uploadTime = LocalDateTime.now();
    }
}
