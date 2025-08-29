-- Dream个人博客系统 - 简化数据库设计
-- 创建时间: 2025-01-17
-- 说明: 根据前端功能需求设计的简化数据库结构

-- 删除已存在的数据库（如果存在）
DROP DATABASE IF EXISTS dream_db;

-- 创建数据库
CREATE DATABASE dream_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE dream_db;

-- ========================================
-- 1. 用户表 (users)
-- ========================================
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键自增',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，用于登录，唯一',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱地址，用于登录和通知，唯一',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希值，存储加密后的密码',
    nickname VARCHAR(100) DEFAULT NULL COMMENT '昵称，用于显示',
    avatar_url VARCHAR(500) DEFAULT NULL COMMENT '头像图片URL',
    bio TEXT DEFAULT NULL COMMENT '个人简介',
    github_url VARCHAR(200) DEFAULT NULL COMMENT 'GitHub链接',
    twitter_url VARCHAR(200) DEFAULT NULL COMMENT 'Twitter链接',
    website_url VARCHAR(200) DEFAULT NULL COMMENT '个人网站链接',
    note_count INT DEFAULT 0 COMMENT '笔记数量统计',
    view_count INT DEFAULT 0 COMMENT '个人主页访问量',
    like_count INT DEFAULT 0 COMMENT '获得的总点赞数',
    is_active BOOLEAN DEFAULT TRUE COMMENT '账户是否激活',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记，0-未删除，1-已删除',
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- ========================================
-- 2. 分类表 (categories)
-- ========================================
CREATE TABLE categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID，主键自增',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '分类名称，唯一',
    description VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    color VARCHAR(7) DEFAULT '#007bff' COMMENT '分类颜色，十六进制格式',
    icon VARCHAR(100) DEFAULT NULL COMMENT '分类图标名称或类名',
    sort_order INT DEFAULT 0 COMMENT '排序顺序，数值越小越靠前',
    note_count INT DEFAULT 0 COMMENT '该分类下的笔记数量',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活显示',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记',
    
    INDEX idx_name (name),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记分类表';

-- ========================================
-- 3. 标签表 (tags)
-- ========================================
CREATE TABLE tags (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID，主键自增',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称，唯一',
    color VARCHAR(7) DEFAULT '#6c757d' COMMENT '标签颜色，十六进制格式',
    usage_count INT DEFAULT 0 COMMENT '标签使用次数统计',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记',
    
    INDEX idx_name (name),
    INDEX idx_usage_count (usage_count DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ========================================
-- 4. 笔记表 (notes)
-- ========================================
CREATE TABLE notes (
    note_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '笔记ID，主键自增',
    title VARCHAR(200) NOT NULL COMMENT '笔记标题',
    content LONGTEXT NOT NULL COMMENT '笔记内容，支持Markdown格式',
    summary VARCHAR(500) DEFAULT NULL COMMENT '笔记摘要，用于列表显示',
    cover_image_url VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    user_id BIGINT NOT NULL COMMENT '作者用户ID，外键关联users表',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID，外键关联categories表',
    status ENUM('draft', 'published', 'archived') DEFAULT 'draft' COMMENT '笔记状态：draft-草稿，published-已发布，archived-已归档',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    is_top BOOLEAN DEFAULT FALSE COMMENT '是否置顶',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    published_at TIMESTAMP NULL DEFAULT NULL COMMENT '发布时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记',
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL,
    
    INDEX idx_user_id (user_id),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at DESC),
    INDEX idx_published_at (published_at DESC),
    FULLTEXT INDEX idx_title_content (title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记表';

-- ========================================
-- 5. 笔记标签关联表 (note_tags)
-- ========================================
CREATE TABLE note_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID，主键自增',
    note_id BIGINT NOT NULL COMMENT '笔记ID，外键关联notes表',
    tag_id BIGINT NOT NULL COMMENT '标签ID，外键关联tags表',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    FOREIGN KEY (note_id) REFERENCES notes(note_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE,
    
    UNIQUE KEY uk_note_tag (note_id, tag_id),
    INDEX idx_note_id (note_id),
    INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='笔记标签关联表';

-- ========================================
-- 6. 壁纸分类表 (wallpaper_categories)
-- ========================================
CREATE TABLE wallpaper_categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '壁纸分类ID，主键自增',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '分类名称，唯一',
    description VARCHAR(500) DEFAULT NULL COMMENT '分类描述',
    icon VARCHAR(100) DEFAULT NULL COMMENT '分类图标',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    wallpaper_count INT DEFAULT 0 COMMENT '该分类下的壁纸数量',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活显示',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记',
    
    INDEX idx_name (name),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='壁纸分类表';

-- ========================================
-- 7. 壁纸表 (wallpapers)
-- ========================================
CREATE TABLE wallpapers (
    wallpaper_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '壁纸ID，主键自增',
    title VARCHAR(200) NOT NULL COMMENT '壁纸标题',
    description VARCHAR(1000) DEFAULT NULL COMMENT '壁纸描述',
    image_url VARCHAR(500) NOT NULL COMMENT '原图URL',
    thumbnail_url VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
    category_id BIGINT DEFAULT NULL COMMENT '分类ID，外键关联wallpaper_categories表',
    file_size BIGINT DEFAULT NULL COMMENT '文件大小，单位字节',
    width INT DEFAULT NULL COMMENT '图片宽度',
    height INT DEFAULT NULL COMMENT '图片高度',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    is_featured BOOLEAN DEFAULT FALSE COMMENT '是否精选',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BOOLEAN DEFAULT FALSE COMMENT '逻辑删除标记',
    
    FOREIGN KEY (category_id) REFERENCES wallpaper_categories(category_id) ON DELETE SET NULL,
    
    INDEX idx_category_id (category_id),
    INDEX idx_created_at (created_at DESC),
    INDEX idx_download_count (download_count DESC),
    INDEX idx_like_count (like_count DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='壁纸表';

-- ========================================
-- 插入初始数据
-- ========================================

-- 插入默认用户
INSERT INTO users (username, email, password_hash, nickname, bio) VALUES 
('admin', 'admin@dream.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGfMEBP.jNEA.P2h8pjKl0.Fy', '柠萌丶浅爱', '欢迎来到我的个人博客！');

-- 插入默认分类
INSERT INTO categories (name, description, color, icon) VALUES 
('技术分享', '编程技术相关文章', '#007bff', 'code'),
('生活随笔', '日常生活感悟', '#28a745', 'heart'),
('学习笔记', '学习过程中的记录', '#ffc107', 'book');

-- 插入默认标签
INSERT INTO tags (name, color) VALUES 
('Vue.js', '#4fc08d'),
('Spring Boot', '#6db33f'),
('MySQL', '#4479a1'),
('前端开发', '#61dafb'),
('后端开发', '#f7df1e');

-- 插入壁纸分类
INSERT INTO wallpaper_categories (name, description, icon) VALUES 
('风景', '自然风光壁纸', 'landscape'),
('动漫', '动漫角色壁纸', 'star'),
('抽象', '抽象艺术壁纸', 'sparkles');

-- 插入示例笔记
INSERT INTO notes (title, content, summary, user_id, category_id, status, published_at) VALUES 
('欢迎来到Dream博客', '# 欢迎\n\n这是我的第一篇博客文章！', '博客系统的第一篇文章', 1, 1, 'published', NOW()),
('Vue 3 学习笔记', '# Vue 3 新特性\n\n## Composition API\n...', 'Vue 3 的新特性介绍', 1, 3, 'published', NOW());

-- 插入笔记标签关联
INSERT INTO note_tags (note_id, tag_id) VALUES 
(2, 1), (2, 4);

COMMIT;

-- 显示创建完成信息
SELECT 'Dream数据库创建完成！' as message;
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as category_count FROM categories;
SELECT COUNT(*) as note_count FROM notes;