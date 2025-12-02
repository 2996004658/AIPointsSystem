package com.example.demo.service;

import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 管理员服务类
 */
@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 管理员登录
     */
    public String login(String username, String password) {
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return jwtUtil.generateToken(username, "ROLE_ADMIN");
    }

    /**
     * 创建管理员 (用于初始化)
     */
    public AdminUser createAdmin(String username, String password) {
        if (adminUserRepository.existsByUsername(username)) {
            throw new RuntimeException("管理员已存在");
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        return adminUserRepository.save(admin);
    }

    /**
     * 初始化默认管理员
     */
    @PostConstruct
    public void initDefaultAdmin() {
        if (!adminUserRepository.existsByUsername("admin")) {
            createAdmin("admin", "admin123");
            System.out.println("Initialized default admin: admin / admin123");
        }
    }
}
