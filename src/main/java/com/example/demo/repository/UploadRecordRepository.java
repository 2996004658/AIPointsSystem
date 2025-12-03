package com.example.demo.repository;

import com.example.demo.entity.UploadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 上传记录Repository接口
 * 提供上传记录的数据访问功能
 */
@Repository
public interface UploadRecordRepository extends JpaRepository<UploadRecord, Long> {

    /**
     * 根据用户ID查询上传记录
     * 
     * @param userId 用户ID
     * @return 该用户的所有上传记录列表
     */
    List<UploadRecord> findByUserId(Long userId);

    /**
     * 根据用户名查询上传记录
     * 
     * @param username 用户名
     * @return 该用户的所有上传记录列表
     */
    List<UploadRecord> findByUsername(String username);
}
