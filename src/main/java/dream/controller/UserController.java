package dream.controller;

import dream.entity.User;
import dream.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User registeredUser = userService.registerUser(user);
            // 不返回密码信息
            registeredUser.setPasswordHash(null);
            
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("data", registeredUser);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 用户登录接口
     * POST /api/users/login
     * @param loginRequest 登录请求（包含用户名/邮箱和密码）
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String usernameOrEmail = loginRequest.get("usernameOrEmail");
            String password = loginRequest.get("password");
            
            if (usernameOrEmail == null || password == null) {
                response.put("success", false);
                response.put("message", "用户名/邮箱和密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            User user = userService.loginUser(usernameOrEmail, password);
            // 不返回密码信息
            user.setPasswordHash(null);
            
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("data", user);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取用户信息接口
     * GET /api/users/{id}
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userService.getById(id);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
            
            // 不返回密码信息
            user.setPasswordHash(null);
            
            response.put("success", true);
            response.put("data", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 根据用户名获取用户信息接口
     * GET /api/users/username/{username}
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userService.findByUsername(username);
            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
            
            User user = userOpt.get();
            // 不返回密码信息
            user.setPasswordHash(null);
            
            response.put("success", true);
            response.put("data", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取所有激活用户列表接口
     * GET /api/users/active
     * @return 激活用户列表
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveUsers() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<User> users = userService.getActiveUsers();
            // 不返回密码信息
            users.forEach(user -> user.setPasswordHash(null));
            
            response.put("success", true);
            response.put("data", users);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户列表失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 更新用户信息接口
     * PUT /api/users/{id}
     * @param id 用户ID
     * @param user 更新的用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User updatedUser = userService.updateUserInfo(id, user);
            // 不返回密码信息
            updatedUser.setPasswordHash(null);
            
            response.put("success", true);
            response.put("message", "用户信息更新成功");
            response.put("data", updatedUser);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 修改密码接口
     * PUT /api/users/{id}/password
     * @param id 用户ID
     * @param passwordRequest 密码修改请求
     * @return 修改结果
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable Long id, 
            @RequestBody Map<String, String> passwordRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String oldPassword = passwordRequest.get("oldPassword");
            String newPassword = passwordRequest.get("newPassword");
            
            if (oldPassword == null || newPassword == null) {
                response.put("success", false);
                response.put("message", "旧密码和新密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            userService.changePassword(id, oldPassword, newPassword);
            
            response.put("success", true);
            response.put("message", "密码修改成功");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 检查用户名是否可用接口
     * GET /api/users/check/username/{username}
     * @param username 用户名
     * @return 可用性检查结果
     */
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean available = userService.isUsernameAvailable(username);
            
            response.put("success", true);
            response.put("available", available);
            response.put("message", available ? "用户名可用" : "用户名已存在");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检查用户名失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 检查邮箱是否可用接口
     * GET /api/users/check/email/{email}
     * @param email 邮箱地址
     * @return 可用性检查结果
     */
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean available = userService.isEmailAvailable(email);
            
            response.put("success", true);
            response.put("available", available);
            response.put("message", available ? "邮箱可用" : "邮箱已存在");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "检查邮箱失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 增加用户访问量接口
     * POST /api/users/{id}/view
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Map<String, Object>> incrementViewCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.incrementViewCount(id);
            
            response.put("success", true);
            response.put("message", "访问量增加成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "访问量增加失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 增加用户点赞数接口
     * POST /api/users/{id}/like
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> incrementLikeCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.incrementLikeCount(id);
            
            response.put("success", true);
            response.put("message", "点赞成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "点赞失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 减少用户点赞数接口
     * DELETE /api/users/{id}/like
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> decrementLikeCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            userService.decrementLikeCount(id);
            
            response.put("success", true);
            response.put("message", "取消点赞成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "取消点赞失败");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}