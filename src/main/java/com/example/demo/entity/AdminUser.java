package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 管理员实体类
 * 对应数据库中的 admin_users 表
 */
@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
public class AdminUser {

    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名 (唯一)
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 密码 (加密存储)
     */
    @Column(nullable = false)
    private String password;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public AdminUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
