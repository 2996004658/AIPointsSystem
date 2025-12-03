package com.example.demo.service;

import com.example.demo.dto.AiImageTemplateDTO;
import com.example.demo.entity.AiImageTemplate;
import com.example.demo.repository.AiImageTemplateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * AI图片模板服务类
 * 处理模板数据的导入和查询
 */
@Service
public class AiImageTemplateService {

    @Autowired
    private AiImageTemplateRepository repository;

    private static final String GITHUB_JSON_URL = "https://raw.githubusercontent.com/glidea/banana-prompt-quicker/main/prompts.json";

    /**
     * 从GitHub导入JSON数据到数据库
     * 
     * @return 导入的记录数量
     * @throws IOException 网络或JSON解析异常
     */
    @Transactional
    public int importFromGitHub() throws IOException {
        // 从URL读取JSON数据
        ObjectMapper objectMapper = new ObjectMapper();
        List<AiImageTemplateDTO> dtoList = objectMapper.readValue(
                new URL(GITHUB_JSON_URL),
                new TypeReference<List<AiImageTemplateDTO>>() {
                });

        // 转换DTO为实体并保存
        List<AiImageTemplate> templates = new ArrayList<>();
        for (AiImageTemplateDTO dto : dtoList) {
            AiImageTemplate template = new AiImageTemplate();
            template.setTitle(dto.getTitle());
            template.setPreview(dto.getPreview());
            template.setPrompt(dto.getPrompt());
            template.setAuthor(dto.getAuthor());
            template.setLink(dto.getLink());
            template.setMode(dto.getMode());
            template.setCategory(dto.getCategory());
            template.setSubCategory(dto.getSubCategory());
            templates.add(template);
        }

        // 批量保存
        List<AiImageTemplate> savedTemplates = repository.saveAll(templates);
        return savedTemplates.size();
    }

    /**
     * 清空所有模板数据
     */
    @Transactional
    public void clearAll() {
        repository.deleteAll();
    }

    /**
     * 重新导入数据（先清空再导入）
     * 
     * @return 导入的记录数量
     * @throws IOException 网络或JSON解析异常
     */
    @Transactional
    public int reimport() throws IOException {
        clearAll();
        return importFromGitHub();
    }

    /**
     * 查询所有模板
     * 
     * @return 所有模板列表
     */
    public List<AiImageTemplate> getAllTemplates() {
        return repository.findAll();
    }

    /**
     * 根据ID查询模板
     * 
     * @param id 模板ID
     * @return 模板对象，不存在则返回null
     */
    public AiImageTemplate getTemplateById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * 根据分类查询模板
     * 
     * @param category 分类名称
     * @return 该分类下的所有模板
     */
    public List<AiImageTemplate> getTemplatesByCategory(String category) {
        return repository.findByCategory(category);
    }

    /**
     * 根据子分类查询模板
     * 
     * @param subCategory 子分类名称
     * @return 该子分类下的所有模板
     */
    public List<AiImageTemplate> getTemplatesBySubCategory(String subCategory) {
        return repository.findBySubCategory(subCategory);
    }

    /**
     * 根据模式查询模板
     * 
     * @param mode 模式（generate/edit）
     * @return 该模式下的所有模板
     */
    public List<AiImageTemplate> getTemplatesByMode(String mode) {
        return repository.findByMode(mode);
    }

    /**
     * 根据标题搜索模板
     * 
     * @param keyword 搜索关键词
     * @return 包含关键词的模板列表
     */
    public List<AiImageTemplate> searchByTitle(String keyword) {
        return repository.findByTitleContaining(keyword);
    }

    /**
     * 获取模板总数
     * 
     * @return 模板总数
     */
    public long getTemplateCount() {
        return repository.count();
    }
}
