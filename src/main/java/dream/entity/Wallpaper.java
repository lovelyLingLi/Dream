package dream.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 壁纸实体类
 * 对应数据库表：wallpapers
 * 
 * @author Dream Team
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wallpapers")
public class Wallpaper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 壁纸ID，主键自增
     */
    @TableId(value = "wallpaper_id", type = IdType.AUTO)
    private Long wallpaperId;

    /**
     * 壁纸标题
     */
    @TableField("title")
    @NotBlank(message = "壁纸标题不能为空")
    @Size(max = 200, message = "壁纸标题长度不能超过200个字符")
    private String title;

    /**
     * 壁纸描述
     */
    @TableField("description")
    @Size(max = 500, message = "壁纸描述长度不能超过500个字符")
    private String description;

    /**
     * 壁纸文件URL
     */
    @TableField("image_url")
    @NotBlank(message = "壁纸文件URL不能为空")
    @Size(max = 500, message = "壁纸文件URL长度不能超过500个字符")
    private String imageUrl;

    /**
     * 缩略图URL
     */
    @TableField("thumbnail_url")
    @Size(max = 500, message = "缩略图URL长度不能超过500个字符")
    private String thumbnailUrl;

    /**
     * 壁纸分类ID，外键关联wallpaper_categories表
     */
    @TableField("category_id")
    @NotNull(message = "壁纸分类ID不能为空")
    private Long categoryId;

    /**
     * 上传者用户ID，外键关联users表
     */
    @TableField("user_id")
    @NotNull(message = "上传者用户ID不能为空")
    private Long userId;

    /**
     * 图片宽度（像素）
     */
    @TableField("width")
    private Integer width;

    /**
     * 图片高度（像素）
     */
    @TableField("height")
    private Integer height;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount = 0;

    /**
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount = 0;

    /**
     * 收藏次数
     */
    @TableField("favorite_count")
    private Integer favoriteCount = 0;

    /**
     * 壁纸状态：pending-待审核，approved-已通过，rejected-已拒绝
     */
    @TableField("status")
    private String status = "pending";

    /**
     * 是否推荐
     */
    @TableField("is_featured")
    private Boolean isFeatured = false;

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
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted = false;
}