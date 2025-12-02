package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 功能控制器
 * 提供受保护的 API 功能，每次调用扣除积分
 */
@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    private UserService userService;

    /**
     * 功能 1
     * 消耗 2 积分
     */
    @PostMapping("/function1")
    public ApiResponse<?> function1(Authentication authentication) {
        try {
            String username = authentication.getName();

            if (!userService.hasEnoughPoints(username, 2)) {
                return ApiResponse.error("积分不足");
            }

            userService.deductPoints(username, 2, "function1");

            System.out.println("=== 功能1被调用 ===");
            System.out.println("用户: " + username);
            System.out.println("扣除积分: 2");
            System.out.println("剩余积分: " + userService.getUserByUsername(username).getPoints());
            System.out.println("==================");

            return ApiResponse.success("功能1执行成功");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 功能 2
     * 消耗 2 积分
     */
    @PostMapping("/function2")
    public ApiResponse<?> function2(Authentication authentication) {
        try {
            String username = authentication.getName();

            if (!userService.hasEnoughPoints(username, 2)) {
                return ApiResponse.error("积分不足");
            }

            userService.deductPoints(username, 2, "function2");

            System.out.println("=== 功能2被调用 ===");
            System.out.println("用户: " + username);
            System.out.println("扣除积分: 2");
            System.out.println("剩余积分: " + userService.getUserByUsername(username).getPoints());
            System.out.println("==================");

            return ApiResponse.success("功能2执行成功");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 功能 3
     * 消耗 2 积分
     */
    @PostMapping("/function3")
    public ApiResponse<?> function3(Authentication authentication) {
        try {
            String username = authentication.getName();

            if (!userService.hasEnoughPoints(username, 2)) {
                return ApiResponse.error("积分不足");
            }

            userService.deductPoints(username, 2, "function3");

            System.out.println("=== 功能3被调用 ===");
            System.out.println("用户: " + username);
            System.out.println("扣除积分: 2");
            System.out.println("剩余积分: " + userService.getUserByUsername(username).getPoints());
            System.out.println("==================");

            return ApiResponse.success("功能3执行成功");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
