package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 积分历史记录实体类
 * 记录用户积分的所有变动
 */
@Entity
@Table(name = "points_history")
@Data
@NoArgsConstructor
public class PointsHistory {

    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户 ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 变动类型 (如: register, function1, qq_login_bonus)
     */
    @Column(nullable = false)
    private String type;

    /**
     * 变动积分数 (正数为增加，负数为扣除)
     */
    @Column(nullable = false)
    private Integer points;

    /**
     * 描述信息
     */
    @Column
    private String description;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public PointsHistory(Long userId, String type, Integer points, String description) {
        this.userId = userId;
        this.type = type;
        this.points = points;
        this.description = description;
    }
}
