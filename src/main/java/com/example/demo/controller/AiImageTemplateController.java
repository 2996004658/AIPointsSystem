package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.AiImageTemplate;
import com.example.demo.service.AiImageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI图片模板控制器
 * 提供模板数据导入和查询的API接口
 */
@RestController
@RequestMapping("/api/ai-template")
@CrossOrigin(origins = "*")
public class AiImageTemplateController {

    @Autowired
    private AiImageTemplateService templateService;

    /**
     * 从GitHub导入模板数据
     * 
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseEntity<ApiResponse<Map<String, Object>>> importTemplates() {
        try {
            int count = templateService.importFromGitHub();

            Map<String, Object> result = new HashMap<>();
            result.put("importedCount", count);
            result.put("totalCount", templateService.getTemplateCount());

            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "数据导入成功", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "导入失败：" + e.getMessage(), null));
        }
    }

    /**
     * 重新导入数据（先清空再导入）
     * 
     * @return 导入结果
     */
    @PostMapping("/reimport")
    public ResponseEntity<ApiResponse<Map<String, Object>>> reimportTemplates() {
        try {
            int count = templateService.reimport();

            Map<String, Object> result = new HashMap<>();
            result.put("importedCount", count);
            result.put("totalCount", templateService.getTemplateCount());

            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "数据重新导入成功", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "重新导入失败：" + e.getMessage(), null));
        }
    }

    /**
     * 查询所有模板
     * 
     * @return 所有模板列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AiImageTemplate>>> getAllTemplates() {
        try {
            List<AiImageTemplate> templates = templateService.getAllTemplates();
            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "查询成功", templates));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 根据ID查询模板
     * 
     * @param id 模板ID
     * @return 模板详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AiImageTemplate>> getTemplateById(@PathVariable Long id) {
        try {
            AiImageTemplate template = templateService.getTemplateById(id);
            if (template == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, false, "模板不存在", null));
            }
            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "查询成功", template));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 根据分类查询模板
     * 
     * @param category 分类名称
     * @return 该分类下的模板列表
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<AiImageTemplate>>> getTemplatesByCategory(
            @PathVariable String category) {
        try {
            List<AiImageTemplate> templates = templateService.getTemplatesByCategory(category);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "查询成功", templates));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 根据模式查询模板
     * 
     * @param mode 模式（generate/edit）
     * @return 该模式下的模板列表
     */
    @GetMapping("/mode/{mode}")
    public ResponseEntity<ApiResponse<List<AiImageTemplate>>> getTemplatesByMode(
            @PathVariable String mode) {
        try {
            List<AiImageTemplate> templates = templateService.getTemplatesByMode(mode);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "查询成功", templates));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 搜索模板
     * 
     * @param keyword 搜索关键词
     * @return 包含关键词的模板列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AiImageTemplate>>> searchTemplates(
            @RequestParam String keyword) {
        try {
            List<AiImageTemplate> templates = templateService.searchByTitle(keyword);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "搜索成功", templates));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "搜索失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取统计信息
     * 
     * @return 统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCount", templateService.getTemplateCount());

            return ResponseEntity.ok(
                    new ApiResponse<>(200, true, "查询成功", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }
}
