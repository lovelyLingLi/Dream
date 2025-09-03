package dream.common;

import dream.exception.ErrorCode;

/**
 * 统一响应结果类 - 标准化API返回格式
 * 
 * 这个类用于统一所有API接口的返回格式，包括：
 * - 成功/失败状态
 * - 错误码
 * - 返回数据
 * - 错误消息
 * 
 * 使用统一响应格式的好处：
 * 1. 前端可以统一处理响应结果
 * 2. 便于接口文档编写和维护
 * 3. 提高代码的一致性和可维护性
 * 
 * @param <T> 返回数据的类型
 * @author Dream开发团队
 * @version 1.0
 * @since 2025-01-17
 */
public class Result<T> {
    
    /**
     * 响应状态码
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 默认构造函数
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 带参数的构造函数
     * 
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }
    
    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应（自定义消息）
     * 
     * @param message 自定义消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 失败响应（使用错误码枚举）
     * 
     * @param errorCode 错误码枚举
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
    
    /**
     * 失败响应（自定义错误码和消息）
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(String code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 失败响应（使用错误码枚举和自定义消息）
     * 
     * @param errorCode 错误码枚举
     * @param customMessage 自定义错误消息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> error(ErrorCode errorCode, String customMessage) {
        return new Result<>(errorCode.getCode(), customMessage, null);
    }
    
    /**
     * 判断是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ErrorCode.SUCCESS.getCode().equals(this.code);
    }
    
    /**
     * 判断是否失败
     * 
     * @return 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
    
    // ========== Getter和Setter方法 ==========
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 重写toString方法
     * 
     * @return 对象的字符串表示
     */
    @Override
    public String toString() {
        return String.format("Result{code='%s', message='%s', data=%s, timestamp=%d}", 
                           code, message, data, timestamp);
    }
}