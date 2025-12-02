package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的 users 表
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名 (唯一)
     */
    @Column(unique = true)
    private String username;

    /**
     * 密码 (加密存储)
     */
    @Column
    private String password;

    /**
     * QQ OpenID (唯一)
     */
    @Column(unique = true)
    private String openid;

    /**
     * 设备 ID
     */
    @Column
    private String deviceId;

    /**
     * 用户积分，默认为 10
     */
    @Column(nullable = false)
    private Integer points = 10;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = 10;
    }
}
