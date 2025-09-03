package dream.exception;

public enum ErrorCode {
    
    // ========== 通用错误码 ==========

    SUCCESS("200", 200, "操作成功"), // 200 成功
    INTERNAL_ERROR("500", 500, "系统内部错误"), // 500 系统内部错误
    PARAM_ERROR("400", 400, "参数错误"), // 400 参数错误
    UNAUTHORIZED("401", 401, "未授权访问"), // 401 未授权访问
    FORBIDDEN("403", 403, "禁止访问"), // 403 禁止访问
    NOT_FOUND("404", 404, "资源不存在"), // 404 资源不存在
    
    // ========== 用户相关错误码 ==========

    USER_NOT_FOUND("U001", 404, "用户不存在"), // 404 用户不存在
    USERNAME_EXISTS("U002", 400, "用户名已存在"), // 400 用户名已存在
    EMAIL_EXISTS("U003", 400, "邮箱已存在"), // 400 邮箱已存在
    PASSWORD_ERROR("U004", 400, "密码错误"), // 400 密码错误
    USER_DISABLED("U005", 403, "用户已被禁用"), // 403 用户已被禁用
    
    // ========== 数据验证错误码 ==========

    VALIDATION_ERROR("V001", 400, "数据验证失败"), // 400 数据验证失败
    REQUIRED_FIELD_EMPTY("V002", 400, "必填字段为空"), // 400 必填字段为空
    FIELD_LENGTH_EXCEEDED("V003", 400, "字段长度超限"), // 400 字段长度超限
    EMAIL_FORMAT_ERROR("V004", 400, "邮箱格式错误"), // 400 邮箱格式错误
    
    // ========== 业务逻辑错误码 ==========

    BUSINESS_RULE_VIOLATION("B001", 400, "业务规则违反"), // 400 业务规则违反
    OPERATION_NOT_ALLOWED("B002", 403, "操作不被允许"), // 403 操作不被允许
    DATA_STATUS_ERROR("B003", 400, "数据状态异常"); // 400 数据状态异常
    
    /**
     * 错误码
     */
    private final String code;
    
    /**
     * HTTP状态码
     */
    private final int httpStatus;
    
    /**
     * 错误描述
     */
    private final String message;
    
    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param httpStatus HTTP状态码
     * @param message 错误描述
     */
    ErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取HTTP状态码
     * 
     * @return HTTP状态码
     */
    public int getHttpStatus() {
        return httpStatus;
    }
    
    /**
     * 获取错误描述
     * 
     * @return 错误描述
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据错误码查找对应的枚举值
     * 
     * @param code 错误码
     * @return ErrorCode枚举值，如果未找到则返回null
     */
    public static ErrorCode getByCode(String code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
    
    /**
     * 重写toString方法
     * 
     * @return 错误码的字符串表示
     */
    @Override
    public String toString() {
        return String.format("ErrorCode{code='%s', httpStatus=%d, message='%s'}", 
                           code, httpStatus, message);
    }
}