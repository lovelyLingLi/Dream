package dream.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 笔记标签关联实体类
 * 对应数据库表：note_tags
 * 
 * @author Dream Team
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("note_tags")
public class NoteTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID，主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 笔记ID，外键关联notes表
     */
    @TableField("note_id")
    @NotNull(message = "笔记ID不能为空")
    private Long noteId;

    /**
     * 标签ID，外键关联tags表
     */
    @TableField("tag_id")
    @NotNull(message = "标签ID不能为空")
    private Long tagId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}