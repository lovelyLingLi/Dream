package dream.controller;

import dream.common.Result;
import dream.entity.User;
import dream.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户控制器
 * 提供用户相关的REST API接口
 * 包括用户注册、登录、信息查询、更新等功能
 */
@RestController
@RequestMapping("/api/users")  // 所有接口的基础路径
@CrossOrigin(origins = "*")  // 允许跨域请求，方便前端调用
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    /**
     * 用户注册接口
     * POST /api/users/register
     * @param user 用户注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        // 不返回密码信息
        registeredUser.setPasswordHash(null);
        
        return Result.success("注册成功", registeredUser);
    }
    
    /**
     * 用户登录接口
     * POST /api/users/login
     * @param loginRequest 登录请求（包含用户名和密码）
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@Valid @RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        User user = userService.loginUser(username, password);
        // 不返回密码信息
        user.setPasswordHash(null);
        
        return Result.success("登录成功", user);
    }
    
    /**
     * 获取用户信息接口
     * GET /api/users/{id}
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("U001", "用户不存在");
        }
        
        // 不返回密码信息
        user.setPasswordHash(null);
        
        return Result.success(user);
    }
    
    /**
     * 根据用户名获取用户信息接口
     * GET /api/users/username/{username}
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            return Result.error("U001", "用户不存在");
        }
        
        User user = userOpt.get();
        // 不返回密码信息
        user.setPasswordHash(null);
        
        return Result.success(user);
    }
    
    /**
     * 获取所有激活用户列表接口
     * GET /api/users/active
     * @return 激活用户列表
     */
    @GetMapping("/active")
    public Result<List<User>> getActiveUsers() {
        List<User> users = userService.getActiveUsers();
        // 不返回密码信息
        users.forEach(user -> user.setPasswordHash(null));
        
        return Result.success(users);
    }
    
    /**
     * 更新用户信息接口
     * PUT /api/users/{id}
     * @param id 用户ID
     * @param user 更新的用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        user.setId(id); // 确保ID一致
        User updatedUser = userService.updateUserInfo(id, user);
        // 不返回密码信息
        updatedUser.setPasswordHash(null);
        
        return Result.success("用户信息更新成功", updatedUser);
    }
    
    /**
     * 修改密码接口1
     * PUT /api/users/{id}/password
     * @param id 用户ID
     * @param passwordRequest 密码修改请求
     * @return 修改结果
     */
    @PutMapping("/{id}/password")
    public Result<String> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordRequest) {
        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");
        
        userService.changePassword(id, oldPassword, newPassword);
        
        return Result.success("密码修改成功");
    }
    
    /**
     * 检查用户名是否可用接口
     * GET /api/users/check/username/{username}
     * @param username 用户名
     * @return 可用性检查结果
     */
    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsernameAvailability(@PathVariable String username) {
        boolean available = userService.isUsernameAvailable(username);
        String message = available ? "用户名可用" : "用户名已存在";
        
        return Result.success(message, available);
    }
    
    /**
     * 检查邮箱是否可用接口
     * GET /api/users/check/email/{email}
     * @param email 邮箱地址
     * @return 可用性检查结果
     */
    @GetMapping("/check/email/{email}")
    public Result<Boolean> checkEmailAvailability(@PathVariable String email) {
        boolean available = userService.isEmailAvailable(email);
        String message = available ? "邮箱可用" : "邮箱已存在";
        
        return Result.success(message, available);
    }
    
    /**
     * 增加用户访问量接口
     * POST /api/users/{id}/view
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/view")
    public Result<String> incrementViewCount(@PathVariable Long id) {
        userService.incrementViewCount(id);
        
        return Result.success("访问量增加成功");
    }
    
    /**
     * 增加用户点赞数接口
     * POST /api/users/{id}/like
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/like")
    public Result<String> incrementLikeCount(@PathVariable Long id) {
        userService.incrementLikeCount(id);
        
        return Result.success("点赞成功");
    }
    
    /**
     * 减少用户点赞数接口
     * DELETE /api/users/{id}/like
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}/like")
    public Result<String> decrementLikeCount(@PathVariable Long id) {
        userService.decrementLikeCount(id);
        
        return Result.success("取消点赞成功");
    }
    
    /**
     * 停用用户接口
     * PUT /api/users/{id}/deactivate
     * @param id 用户ID
     * @return 停用结果
     */
    @PutMapping("/{id}/deactivate")
    public Result<String> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        
        return Result.success("用户已停用");
    }
    
    /**
     * 激活用户接口
     * PUT /api/users/{id}/activate
     * @param id 用户ID
     * @return 激活结果
     */
    @PutMapping("/{id}/activate")
    public Result<String> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        
        return Result.success("用户已激活");
    }
}