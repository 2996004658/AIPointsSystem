package com.example.demo.repository;

import com.example.demo.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员数据访问接口
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    /**
     * 根据用户名查找管理员
     */
    Optional<AdminUser> findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
}
