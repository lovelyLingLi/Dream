package dream.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dream.entity.User;
import dream.exception.business.UserException;
import dream.mapper.UserMapper;
import dream.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务逻辑实现类
 * 继承MyBatis-Plus的ServiceImpl，获得基础CRUD操作
 * 实现UserService接口，提供用户相关的业务逻辑，如注册、登录、信息更新等
 * 
 * 技术解释：
 * - @Service注解标记这是一个业务逻辑组件，Spring会自动扫描并注册为Bean
 * - @Transactional确保方法执行时的事务一致性，出现异常时自动回滚
 * - ServiceImpl<UserMapper, User>提供了基础的CRUD操作实现
 */
@Service
@Transactional  // 事务管理注解，确保数据一致性
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    
    @Autowired
    private UserMapper userMapper;
    
    // 密码加密器，用于密码的加密和验证
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     * @throws UserException 当用户名或邮箱已存在时抛出异常
     */
    public User registerUser(User user) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            throw UserException.usernameExists();
        }
        
        // 检查邮箱是否已存在
        if (userMapper.existsByEmail(user.getEmail())) {
            throw UserException.emailExists();
        }
        
        // 加密密码
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        
        // 设置默认值
        if (user.getNickname() == null || user.getNickname().trim().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        user.setIsActive(true);
        user.setNoteCount(0);
        user.setViewCount(0);
        user.setLikeCount(0);
        
        // 保存用户
        userMapper.insert(user);
        return user;
    }
    
    /**
     * 用户登录验证
     * @param usernameOrEmail 用户名或邮箱
     * @param password 原始密码
     * @return 用户信息（如果验证成功）
     * @throws UserException 当用户不存在或密码错误时抛出异常
     */
    public User loginUser(String usernameOrEmail, String password) {
        // 查找用户（通过用户名或邮箱）
        Optional<User> userOpt = userMapper.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (!userOpt.isPresent()) {
            throw UserException.userNotFound();
        }
        
        User user = userOpt.get();
        
        // 检查用户是否被禁用
        if (!user.getIsActive()) {
            throw UserException.userDisabled();
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw UserException.passwordError();
        }
        
        return user;
    }
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 用户信息
     */
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
    
    /**
     * 更新用户基本信息
     * @param userId 用户ID
     * @param user 更新的用户信息
     * @return 更新后的用户信息
     * @throws UserException 当用户不存在时抛出异常
     */
    public User updateUserInfo(Long userId, User user) {
        User existingUser = userMapper.selectById(userId);
        if (existingUser == null) {
            throw UserException.userNotFound();
        }
        
        // 更新允许修改的字段
        if (user.getNickname() != null) {
            existingUser.setNickname(user.getNickname());
        }
        if (user.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(user.getAvatarUrl());
        }
        if (user.getBio() != null) {
            existingUser.setBio(user.getBio());
        }
        if (user.getGithubUrl() != null) {
            existingUser.setGithubUrl(user.getGithubUrl());
        }
        if (user.getTwitterUrl() != null) {
            existingUser.setTwitterUrl(user.getTwitterUrl());
        }
        if (user.getWebsiteUrl() != null) {
            existingUser.setWebsiteUrl(user.getWebsiteUrl());
        }
        
        userMapper.updateById(existingUser);
        return existingUser;
    }
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @throws UserException 当用户不存在或旧密码错误时抛出异常
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw UserException.userNotFound();
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw UserException.passwordError();
        }
        
        // 更新密码
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
    
    /**
     * 获取所有激活用户
     * @return 激活用户列表
     */
    public List<User> getActiveUsers() {
        return userMapper.findByIsActiveOrderByCreatedAtDesc(true);
    }
    
    /**
     * 禁用用户账户
     * @param userId 用户ID
     * @throws UserException 当用户不存在时抛出异常
     */
    public void deactivateUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw UserException.userNotFound();
        }
        
        user.setIsActive(false);
        userMapper.updateById(user);
    }
    
    /**
     * 激活用户账户
     * @param userId 用户ID
     * @throws UserException 当用户不存在时抛出异常
     */
    public void activateUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw UserException.userNotFound();
        }
        
        user.setIsActive(true);
        userMapper.updateById(user);
    }
    
    /**
     * 增加用户访问量
     * @param userId 用户ID
     */
    public void incrementViewCount(Long userId) {
        userMapper.incrementViewCount(userId);
    }
    
    /**
     * 增加用户点赞数
     * @param userId 用户ID
     */
    public void incrementLikeCount(Long userId) {
        userMapper.incrementLikeCount(userId);
    }
    
    /**
     * 减少用户点赞数
     * @param userId 用户ID
     */
    public void decrementLikeCount(Long userId) {
        userMapper.decrementLikeCount(userId);
    }
    
    /**
     * 更新用户统计信息
     * @param userId 用户ID
     * @param noteCount 笔记数量
     * @param viewCount 访问量
     * @param likeCount 点赞数
     */
    public void updateUserStats(Long userId, Integer noteCount, Integer viewCount, Integer likeCount) {
        userMapper.updateUserStats(userId, noteCount, viewCount, likeCount);
    }
    
    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 是否可用
     */
    public boolean isUsernameAvailable(String username) {
        return !userMapper.existsByUsername(username);
    }
    
    /**
     * 检查邮箱是否可用
     * @param email 邮箱地址
     * @return 是否可用
     */
    public boolean isEmailAvailable(String email) {
        return !userMapper.existsByEmail(email);
    }
}