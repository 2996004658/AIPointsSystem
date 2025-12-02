package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.PointsHistory;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 积分控制器
 * 提供积分查询、历史记录等接口
 */
@RestController
@RequestMapping("/api/points")
public class PointsController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取积分历史记录 (分页)
     * 
     * @param token JWT Token
     * @param page  页码
     * @param size  每页大小
     */
    @GetMapping("/history")
    public ApiResponse<?> getHistory(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            Page<PointsHistory> history = userService.getPointsHistory(username, page, size);
            return ApiResponse.success("获取成功", history);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
