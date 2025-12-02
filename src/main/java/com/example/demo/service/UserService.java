package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 * 处理用户注册、登录、积分管理等业务逻辑
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private com.example.demo.repository.PointsHistoryRepository pointsHistoryRepository;

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 密码
     * @return 注册成功的用户对象
     */
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPoints(10);

        User savedUser = userRepository.save(user);

        // 记录历史
        pointsHistoryRepository.save(new com.example.demo.entity.PointsHistory(
                savedUser.getId(), "register", 10, "用户注册赠送"));

        return savedUser;
    }

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return JWT Token
     */
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return jwtUtil.generateToken(username);
    }

    /**
     * 扣除积分
     * 
     * @param username 用户名
     * @param points   扣除的点数
     * @param type     消费类型
     */
    @Transactional
    public synchronized void deductPoints(String username, int points, String type) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getPoints() < points) {
            throw new RuntimeException("积分不足");
        }

        user.setPoints(user.getPoints() - points);
        userRepository.save(user);

        // 记录历史
        pointsHistoryRepository.save(new com.example.demo.entity.PointsHistory(
                user.getId(), type, -points, "功能消耗"));
    }

    /**
     * 获取用户信息
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 检查积分是否充足
     */
    public boolean hasEnoughPoints(String username, int requiredPoints) {
        User user = getUserByUsername(username);
        return user.getPoints() >= requiredPoints;
    }

    /**
     * QQ 登录/注册
     * 
     * @param openid   QQ OpenID
     * @param deviceId 设备 ID
     * @return JWT Token
     */
    public String qqLogin(String openid, String deviceId) {
        // 1. 根据 openid 检查用户是否存在
        return userRepository.findByOpenid(openid)
                .map(user -> jwtUtil.generateToken(user.getUsername()))
                .orElseGet(() -> {
                    // 2. 新用户逻辑
                    User newUser = new User();
                    newUser.setOpenid(openid);
                    newUser.setDeviceId(deviceId);

                    // 为 QQ 用户生成唯一用户名
                    String randomUsername = "qq_" + openid.substring(0, Math.min(8, openid.length())) + "_"
                            + System.currentTimeMillis();
                    newUser.setUsername(randomUsername);

                    int initialPoints = 0;
                    String desc = "QQ注册(旧设备)";

                    // 检查设备是否存在
                    if (userRepository.existsByDeviceId(deviceId)) {
                        newUser.setPoints(0); // 设备已存在 -> 0 积分
                    } else {
                        newUser.setPoints(10); // 新设备 -> 10 积分
                        initialPoints = 10;
                        desc = "QQ注册(新设备)";
                    }

                    User savedUser = userRepository.save(newUser);

                    // 记录历史
                    pointsHistoryRepository.save(new com.example.demo.entity.PointsHistory(
                            savedUser.getId(), "qq_login_bonus", initialPoints, desc));

                    return jwtUtil.generateToken(newUser.getUsername());
                });
    }

    /**
     * 获取积分历史记录
     * 
     * @param username 用户名
     * @param page     页码 (从0开始)
     * @param size     每页大小
     * @return 分页的历史记录
     */
    /**
     * 获取积分历史记录
     * 
     * @param username 用户名
     * @param page     页码 (从0开始)
     * @param size     每页大小
     * @return 分页的历史记录
     */
    public org.springframework.data.domain.Page<com.example.demo.entity.PointsHistory> getPointsHistory(String username,
            int page, int size) {
        User user = getUserByUsername(username);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
    }

    /**
     * 获取所有用户 (用于后台管理)
     */
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 获取所有积分历史 (用于后台管理)
     */
    public org.springframework.data.domain.Page<com.example.demo.dto.PointsHistoryDTO> getAllPointsHistory(int page,
            int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by("createdAt").descending());
        org.springframework.data.domain.Page<com.example.demo.entity.PointsHistory> historyPage = pointsHistoryRepository
                .findAll(pageable);

        // 获取所有用户并转为 Map
        java.util.Map<Long, String> userMap = userRepository.findAll().stream()
                .collect(java.util.stream.Collectors.toMap(User::getId, User::getUsername));

        return historyPage.map(history -> new com.example.demo.dto.PointsHistoryDTO(
                history.getId(),
                history.getUserId(),
                userMap.getOrDefault(history.getUserId(), "Unknown"),
                history.getType(),
                history.getPoints(),
                history.getDescription(),
                history.getCreatedAt()));
    }
}
