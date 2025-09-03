package dream.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 壁纸分类实体类
 * 对应数据库表：wallpaper_categories
 * 
 * @author Dream Team
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wallpaper_categories")
public class WallpaperCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 壁纸分类ID，主键自增
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称，唯一
     */
    @TableField("name")
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;

    /**
     * 分类描述
     */
    @TableField("description")
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;

    /**
     * 分类图标
     */
    @TableField("icon")
    @Size(max = 100, message = "图标名称长度不能超过100个字符")
    private String icon;

    /**
     * 排序顺序
     */
    @TableField("sort_order")
    private Integer sortOrder = 0;

    /**
     * 该分类下的壁纸数量
     */
    @TableField("wallpaper_count")
    private Integer wallpaperCount = 0;

    /**
     * 是否激活显示
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
     * 逻辑删除标记
     */
    @TableLogic
    @TableField("deleted")
    private Boolean deleted = false;
}