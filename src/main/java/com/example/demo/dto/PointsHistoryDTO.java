package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 积分历史记录 DTO
 * 包含用户名信息
 */
@Data
public class PointsHistoryDTO {
    private Long id;
    private Long userId;
    private String username;
    private String type;
    private Integer points;
    private String description;
    private LocalDateTime createdAt;

    public PointsHistoryDTO(Long id, Long userId, String username, String type, Integer points, String description,
            LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.points = points;
        this.description = description;
        this.createdAt = createdAt;
    }
}
