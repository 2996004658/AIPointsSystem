package com.example.demo.service;

import com.example.demo.entity.UploadRecord;
import com.example.demo.repository.UploadRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 上传记录服务类
 * 管理文件上传记录的业务逻辑
 */
@Service
public class UploadRecordService {

    @Autowired
    private UploadRecordRepository uploadRecordRepository;

    /**
     * 保存上传记录
     * 
     * @param uploadRecord 上传记录对象
     * @return 保存后的上传记录（包含自动生成的ID）
     */
    @Transactional
    public UploadRecord saveUploadRecord(UploadRecord uploadRecord) {
        return uploadRecordRepository.save(uploadRecord);
    }

    /**
     * 根据用户ID查询上传记录
     * 
     * @param userId 用户ID
     * @return 该用户的所有上传记录
     */
    public List<UploadRecord> getUploadRecordsByUserId(Long userId) {
        return uploadRecordRepository.findByUserId(userId);
    }

    /**
     * 根据用户名查询上传记录
     * 
     * @param username 用户名
     * @return 该用户的所有上传记录
     */
    public List<UploadRecord> getUploadRecordsByUsername(String username) {
        return uploadRecordRepository.findByUsername(username);
    }

    /**
     * 查询所有上传记录
     * 
     * @return 所有上传记录列表
     */
    public List<UploadRecord> getAllUploadRecords() {
        return uploadRecordRepository.findAll();
    }

    /**
     * 根据ID查询上传记录
     * 
     * @param id 记录ID
     * @return 上传记录，如果不存在则返回null
     */
    public UploadRecord getUploadRecordById(Long id) {
        return uploadRecordRepository.findById(id).orElse(null);
    }
}
