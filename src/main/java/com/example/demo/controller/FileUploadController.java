package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UploadResponse;
import com.example.demo.entity.UploadRecord;
import com.example.demo.service.OssService;
import com.example.demo.service.UploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传控制器
 * 提供图片上传到OSS的API接口
 */
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private OssService ossService;

    @Autowired
    private UploadRecordService uploadRecordService;

    /**
     * 上传图片到OSS
     * 需要用户登录后才能上传
     * 
     * @param file 要上传的图片文件
     * @return 上传结果，包含文件URL等信息
     */
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<UploadResponse>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            // 验证用户是否登录
            String username = getCurrentUsername();
            if (username == null || username.equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(401, false, "请先登录后再上传图片", null));
            }

            // 上传文件到OSS
            String ossFilePath = ossService.uploadImage(file);

            // 生成文件访问URL
            String fileUrl = ossService.getFileUrl(ossFilePath);

            Long userId = null; // 如果需要，可以从用户服务获取userId

            // 创建上传记录
            UploadRecord uploadRecord = new UploadRecord();
            uploadRecord.setOriginalFileName(file.getOriginalFilename());
            uploadRecord.setOssFilePath(ossFilePath);
            uploadRecord.setFileUrl(fileUrl);
            uploadRecord.setFileSize(file.getSize());
            uploadRecord.setContentType(file.getContentType());
            uploadRecord.setUsername(username);
            uploadRecord.setUserId(userId);

            // 保存上传记录到数据库
            UploadRecord savedRecord = uploadRecordService.saveUploadRecord(uploadRecord);

            // 构建响应对象
            UploadResponse response = new UploadResponse(
                    savedRecord.getId(),
                    fileUrl,
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    savedRecord.getUploadTime());

            return ResponseEntity.ok(new ApiResponse<>(200, true, "图片上传成功", response));

        } catch (IllegalArgumentException e) {
            // 文件验证失败
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, false, e.getMessage(), null));

        } catch (Exception e) {
            // 其他异常
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "图片上传失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取当前用户的上传记录
     * 
     * @return 上传记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<ApiResponse<List<UploadRecord>>> getMyUploadRecords() {
        try {
            String username = getCurrentUsername();
            if (username == null || username.equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(401, false, "请先登录", null));
            }

            List<UploadRecord> records = uploadRecordService.getUploadRecordsByUsername(username);
            return ResponseEntity.ok(new ApiResponse<>(200, true, "查询成功", records));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取所有上传记录（管理员功能）
     * 
     * @return 所有上传记录列表
     */
    @GetMapping("/records/all")
    public ResponseEntity<ApiResponse<List<UploadRecord>>> getAllUploadRecords() {
        try {
            List<UploadRecord> records = uploadRecordService.getAllUploadRecords();
            return ResponseEntity.ok(new ApiResponse<>(200, true, "查询成功", records));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, false, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取当前登录用户的用户名
     * 
     * @return 用户名，如果未登录则返回null
     */
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return null;
    }
}
