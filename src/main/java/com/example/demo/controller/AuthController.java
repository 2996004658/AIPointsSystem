package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理注册、登录、QQ登录请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private com.example.demo.util.JwtUtil jwtUtil;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request.getUsername(), request.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("points", user.getPoints());
            return ApiResponse.success("注册成功", data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request.getUsername(), request.getPassword());
            User user = userService.getUserByUsername(request.getUsername());

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", user.getUsername());
            data.put("points", user.getPoints());

            return ApiResponse.success("登录成功", data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * QQ 登录/注册接口
     */
    @PostMapping("/qq-login")
    public ApiResponse<?> qqLogin(@RequestBody com.example.demo.dto.QqLoginRequest request) {
        try {
            if (request.getOpenid() == null || request.getDeviceId() == null) {
                return ApiResponse.error("openid和deviceId不能为空");
            }

            String token = userService.qqLogin(request.getOpenid(), request.getDeviceId());
            String username = jwtUtil.extractUsername(token);
            int points = userService.getUserByUsername(username).getPoints();

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", username);
            data.put("points", points);

            return ApiResponse.success("QQ登录成功", data);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}