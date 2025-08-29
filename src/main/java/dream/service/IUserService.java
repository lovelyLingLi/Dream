package dream.service;

import dream.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务逻辑接口
 * 继承MyBatis-Plus的IService接口，获得基础CRUD操作
 * 定义用户相关的业务逻辑方法，如注册、登录、信息更新等
 * 
 * 技术解释：
 * - 接口定义了业务逻辑的规范，实现类负责具体实现
 * - 这种设计模式便于单元测试、依赖注入和代码解耦
 * - IService<User>提供了基础的CRUD方法，如save()、getById()等
 */
public interface IUserService extends IService<User> {
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     * @throws RuntimeException 当用户名或邮箱已存在时抛出异常
     */
    User registerUser(User user);
    
    /**
     * 用户登录验证
     * @param usernameOrEmail 用户名或邮箱
     * @param password 原始密码
     * @return 用户信息（如果验证成功）
     * @throws RuntimeException 当用户不存在或密码错误时抛出异常
     */
    User loginUser(String usernameOrEmail, String password);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 用户信息
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 更新用户基本信息
     * @param userId 用户ID
     * @param user 更新的用户信息
     * @return 更新后的用户信息
     * @throws RuntimeException 当用户不存在时抛出异常
     */
    User updateUserInfo(Long userId, User user);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @throws RuntimeException 当用户不存在或旧密码错误时抛出异常
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 获取所有激活用户
     * @return 激活用户列表
     */
    List<User> getActiveUsers();
    
    /**
     * 禁用用户账户
     * @param userId 用户ID
     * @throws RuntimeException 当用户不存在时抛出异常
     */
    void deactivateUser(Long userId);
    
    /**
     * 激活用户账户
     * @param userId 用户ID
     * @throws RuntimeException 当用户不存在时抛出异常
     */
    void activateUser(Long userId);
    
    /**
     * 增加用户访问量
     * @param userId 用户ID
     */
    void incrementViewCount(Long userId);
    
    /**
     * 增加用户点赞数
     * @param userId 用户ID
     */
    void incrementLikeCount(Long userId);
    
    /**
     * 减少用户点赞数
     * @param userId 用户ID
     */
    void decrementLikeCount(Long userId);
    
    /**
     * 更新用户统计信息
     * @param userId 用户ID
     * @param noteCount 笔记数量
     * @param viewCount 访问量
     * @param likeCount 点赞数
     */
    void updateUserStats(Long userId, Integer noteCount, Integer viewCount, Integer likeCount);
    
    /**
     * 检查用户名是否可用
     * @param username 用户名
     * @return 是否可用
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * 检查邮箱是否可用
     * @param email 邮箱地址
     * @return 是否可用
     */
    boolean isEmailAvailable(String email);
}