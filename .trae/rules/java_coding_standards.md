## 1. 命名规范

### 1.1 基本规则
- **类名**: 大驼峰命名法（UserService, BlogPostController）
- **方法名**: 小驼峰命名法，动词开头（getUserById, updateUserProfile）
- **变量名**: 小驼峰命名法，名词性（userName, userId, createdTime）
- **常量名**: 全大写，下划线分隔（DEFAULT_PASSWORD, MAX_RETRY_COUNT）
- **包名**: 全小写，反向域名（dream.entity, dream.service.impl）

### 1.2 实体类字段命名
- **主键**: 统一使用`id`命名
- **其他字段**: 严格使用小驼峰命名法，避免下划线或全小写
- **示例**: `userName`（正确）而不是`username`（错误），`createdTime`（正确）而不是`created_time`（错误）
- **数据库映射**: 使用@Column注解映射到数据库字段名，实体类字段保持驼峰命名，若已存在映射，则不修改

## 2. 代码结构规范

### 2.1 类结构顺序
1. 静态常量
2. 静态变量
3. 实例变量
4. 构造方法
5. 静态方法
6. 实例方法（public -> protected -> private）

## 3. 代码质量要求

### 3.1 SOLID原则
- 单一职责原则
- 开闭原则
- 依赖倒置原则
- 接口隔离原则

### 3.2 代码可读性
- 保持方法职责单一
- 避免过深的嵌套层级
- 合理拆分复杂逻辑
- 注释清晰，避免过度注释，只有一行不长的注释则在代码后面使用单行注释

### 3.3 性能要求
- 避免循环中数据库操作
- 合理使用缓存
- 字符串拼接使用StringBuilder
