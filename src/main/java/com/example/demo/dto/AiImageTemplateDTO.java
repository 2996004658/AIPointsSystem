package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * AI图片模板DTO
 * 用于接收GitHub JSON数据
 */
@Data
public class AiImageTemplateDTO {

    /**
     * 标题
     */
    private String title;

    /**
     * 预览图URL
     */
    private String preview;

    /**
     * 提示词内容
     */
    private String prompt;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源链接
     */
    private String link;

    /**
     * 模式
     */
    private String mode;

    /**
     * 分类
     */
    private String category;

    /**
     * 子分类
     */
    @JsonProperty("sub_category")
    private String subCategory;
}
