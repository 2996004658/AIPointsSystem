package com.example.demo.repository;

import com.example.demo.entity.PointsHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 积分历史记录数据访问接口
 */
@Repository
public interface PointsHistoryRepository extends JpaRepository<PointsHistory, Long> {
    /**
     * 分页查询用户的积分历史记录，按时间倒序排列
     * 
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 分页的历史记录
     */
    Page<PointsHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
