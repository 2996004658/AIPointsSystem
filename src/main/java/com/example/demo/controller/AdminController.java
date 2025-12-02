package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户列表
     */
    @GetMapping("/users")
    public ApiResponse<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ApiResponse.success("获取用户列表成功", users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取所有积分历史记录 (分页)
     */
    @GetMapping("/points/history")
    public ApiResponse<?> getAllPointsHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<com.example.demo.dto.PointsHistoryDTO> history = userService.getAllPointsHistory(page, size);
            return ApiResponse.success("获取历史记录成功", history);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
