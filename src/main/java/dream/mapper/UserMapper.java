package dream.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dream.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * 用户数据访问层
 * 使用MyBatis-Plus提供基础CRUD操作
 * 继承BaseMapper获得通用方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(@Param("email") String email);
    
    /**
     * 根据用户名或邮箱查找用户
     * @param username 用户名
     * @param email 邮箱地址
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username} OR email = #{email}")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    
    /**
     * 根据激活状态查找用户列表
     * @param isActive 是否激活
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE is_active = #{isActive} ORDER BY created_at DESC")
    java.util.List<User> findByIsActiveOrderByCreatedAtDesc(@Param("isActive") Boolean isActive);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱地址
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(@Param("email") String email);
    
    /**
     * 更新用户统计信息
     * @param userId 用户ID
     * @param noteCount 笔记数量
     * @param viewCount 访问量
     * @param likeCount 点赞数
     */
    @org.apache.ibatis.annotations.Update("UPDATE users SET note_count = #{noteCount}, view_count = #{viewCount}, like_count = #{likeCount} WHERE user_id = #{userId}")
    void updateUserStats(@Param("userId") Long userId, @Param("noteCount") Integer noteCount, @Param("viewCount") Integer viewCount, @Param("likeCount") Integer likeCount);
    
    /**
     * 增加用户访问量
     * @param userId 用户ID
     */
    @org.apache.ibatis.annotations.Update("UPDATE users SET view_count = view_count + 1 WHERE user_id = #{userId}")
    void incrementViewCount(@Param("userId") Long userId);
    
    /**
     * 增加用户点赞数
     * @param userId 用户ID
     */
    @org.apache.ibatis.annotations.Update("UPDATE users SET like_count = like_count + 1 WHERE user_id = #{userId}")
    void incrementLikeCount(@Param("userId") Long userId);
    
    /**
     * 减少用户点赞数
     * @param userId 用户ID
     */
    @org.apache.ibatis.annotations.Update("UPDATE users SET like_count = like_count - 1 WHERE user_id = #{userId} AND like_count > 0")
    void decrementLikeCount(@Param("userId") Long userId);
}