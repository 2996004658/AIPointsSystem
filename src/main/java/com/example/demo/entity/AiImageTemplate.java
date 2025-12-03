package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * AI图片模板实体类
 * 存储AI图片生成的提示词模板
 */
@Entity
@Table(name = "ai_images_template", indexes = @Index(name = "idx_category", columnList = "category"))
@Data
public class AiImageTemplate {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板标题
     */
    @Column(nullable = false)
    private String title;

    /**
     * 预览图URL
     */
    @Column(length = 500)
    private String preview;

    /**
     * 提示词内容（支持长文本）
     */
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String prompt;

    /**
     * 作者
     */
    @Column(length = 100)
    private String author;

    /**
     * 来源链接
     */
    @Column(length = 500)
    private String link;

    /**
     * 模式（generate/edit）
     */
    @Column(length = 50)
    private String mode;

    /**
     * 分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 子分类（可为空）
     */
    @Column(name = "sub_category", length = 50)
    private String subCategory;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 在持久化之前自动设置创建时间
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    /**
     * 在更新之前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
