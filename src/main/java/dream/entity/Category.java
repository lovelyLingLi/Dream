package dream.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体类
 * 对应数据库表：categories
 * 
 * @author Dream Team
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("categories")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID，主键自增
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

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
     * 分类颜色，十六进制格式
     */
    @TableField("color")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式必须为十六进制格式，如#007bff")
    private String color = "#007bff";

    /**
     * 分类图标名称或类名
     */
    @TableField("icon")
    @Size(max = 100, message = "图标名称长度不能超过100个字符")
    private String icon;

    /**
     * 排序顺序，数值越小越靠前
     */
    @TableField("sort_order")
    private Integer sortOrder = 0;

    /**
     * 该分类下的笔记数量
     */
    @TableField("note_count")
    private Integer noteCount = 0;

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