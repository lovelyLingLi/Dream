package dream.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的users表
 * 根据简化数据库设计重新定义字段
 */
@Data  // Lombok注解，自动生成getter、setter、toString等方法
@NoArgsConstructor  // Lombok注解，生成无参构造函数
@AllArgsConstructor  // Lombok注解，生成全参构造函数
@TableName("users")  // MyBatis-Plus注解，指定对应的数据库表名
public class User {
    
    /**
     * 用户ID - 主键，自增
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名 - 用于登录，唯一
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50字符之间")
    @TableField("username")
    private String username;
    
    /**
     * 邮箱地址 - 用于登录和通知，唯一
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    @TableField("email")
    private String email;
    
    /**
     * 密码哈希值 - 存储加密后的密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6字符")
    @TableField("password_hash")
    private String passwordHash;
    
    /**
     * 昵称 - 用于显示
     */
    @Size(max = 100, message = "昵称长度不能超过100字符")
    @TableField("nickname")
    private String nickname;
    
    /**
     * 头像图片URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500字符")
    @TableField("avatar_url")
    private String avatarUrl;
    
    /**
     * 个人简介
     */
    @TableField("bio")
    private String bio;
    
    /**
     * GitHub链接
     */
    @Size(max = 200, message = "GitHub链接长度不能超过200字符")
    @TableField("github_url")
    private String githubUrl;
    
    /**
     * Twitter链接
     */
    @Size(max = 200, message = "Twitter链接长度不能超过200字符")
    @TableField("twitter_url")
    private String twitterUrl;
    
    /**
     * 个人网站链接
     */
    @Size(max = 200, message = "个人网站链接长度不能超过200字符")
    @TableField("website_url")
    private String websiteUrl;
    
    /**
     * 笔记数量统计
     */
    @Min(value = 0, message = "笔记数量不能为负数")
    @TableField("note_count")
    private Integer noteCount = 0;
    
    /**
     * 个人主页访问量
     */
    @Min(value = 0, message = "访问量不能为负数")
    @TableField("view_count")
    private Integer viewCount = 0;
    
    /**
     * 获得的总点赞数
     */
    @Min(value = 0, message = "点赞数不能为负数")
    @TableField("like_count")
    private Integer likeCount = 0;
    
    /**
     * 账户是否激活
     */
    @TableField("is_active")
    private Boolean isActive = true;
    
    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 逻辑删除标记 (0-未删除, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer isDeleted = 0;
}