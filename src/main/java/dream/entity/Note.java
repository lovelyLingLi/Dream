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
 * 笔记实体类
 * 对应数据库表：notes
 * 
 * @author Dream Team
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notes")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 笔记ID，主键自增
     */
    @TableId(value = "note_id", type = IdType.AUTO)
    private Long noteId;

    /**
     * 笔记标题
     */
    @TableField("title")
    @NotBlank(message = "笔记标题不能为空")
    @Size(max = 200, message = "笔记标题长度不能超过200个字符")
    private String title;

    /**
     * 笔记内容，支持Markdown格式
     */
    @TableField("content")
    @NotBlank(message = "笔记内容不能为空")
    private String content;

    /**
     * 笔记摘要，用于列表显示
     */
    @TableField("summary")
    @Size(max = 500, message = "笔记摘要长度不能超过500个字符")
    private String summary;

    /**
     * 封面图片URL
     */
    @TableField("cover_image_url")
    @Size(max = 500, message = "封面图片URL长度不能超过500个字符")
    private String coverImageUrl;

    /**
     * 作者用户ID，外键关联users表
     */
    @TableField("user_id")
    @NotNull(message = "作者用户ID不能为空")
    private Long userId;

    /**
     * 分类ID，外键关联categories表
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 笔记状态：draft-草稿，published-已发布，archived-已归档
     */
    @TableField("status")
    private String status = "draft";

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount = 0;

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
     * 是否置顶
     */
    @TableField("is_top")
    private Boolean isTop = false;

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
     * 发布时间
     */
    @TableField("published_at")
    private LocalDateTime publishedAt;

    /**
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted = false;
}