package com.example.demo.repository;

import com.example.demo.entity.AiImageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AI图片模板Repository接口
 * 提供AI图片模板的数据访问功能
 */
@Repository
public interface AiImageTemplateRepository extends JpaRepository<AiImageTemplate, Long> {

    /**
     * 根据分类查询模板
     * 
     * @param category 分类名称
     * @return 该分类下的所有模板
     */
    List<AiImageTemplate> findByCategory(String category);

    /**
     * 根据子分类查询模板
     * 
     * @param subCategory 子分类名称
     * @return 该子分类下的所有模板
     */
    List<AiImageTemplate> findBySubCategory(String subCategory);

    /**
     * 根据模式查询模板
     * 
     * @param mode 模式（generate/edit）
     * @return 该模式下的所有模板
     */
    List<AiImageTemplate> findByMode(String mode);

    /**
     * 根据分类和子分类查询模板
     * 
     * @param category    分类
     * @param subCategory 子分类
     * @return 符合条件的模板列表
     */
    List<AiImageTemplate> findByCategoryAndSubCategory(String category, String subCategory);

    /**
     * 根据标题模糊查询
     * 
     * @param title 标题关键词
     * @return 包含关键词的模板列表
     */
    List<AiImageTemplate> findByTitleContaining(String title);
}
