package dream.exception.business;

import dream.exception.BaseException;
import dream.exception.ErrorCode;


public class UserException extends BaseException {
    
    /**
     * 默认构造函数
     */
    public UserException() {
        super();
    }
    
    /**
     * 带错误消息的构造函数
     * 
     * @param message 错误消息
     */
    public UserException(String message) {
        super(message);
    }
    
    /**
     * 带错误码和错误消息的构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public UserException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * 带异常原因的构造函数
     * 
     * @param message 错误消息
     * @param cause 异常原因
     */
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 完整的构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public UserException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * 根据错误码枚举创建用户异常
     * 
     * @param errorCode 错误码枚举
     * @return UserException实例
     */
    public static UserException of(ErrorCode errorCode) {
        return new UserException(errorCode.getCode(), errorCode.getMessage());
    }
    
    /**
     * 根据错误码枚举和自定义消息创建用户异常
     * 
     * @param errorCode 错误码枚举
     * @param customMessage 自定义错误消息
     * @return UserException实例
     */
    public static UserException of(ErrorCode errorCode, String customMessage) {
        return new UserException(errorCode.getCode(), customMessage);
    }
    
    // ========== 便捷方法 - 常用用户异常 ==========
    
    /**
     * 用户不存在异常
     * 
     * @return UserException实例
     */
    public static UserException userNotFound() {
        return UserException.of(ErrorCode.USER_NOT_FOUND);
    }
    
    /**
     * 用户名已存在异常
     * 
     * @return UserException实例
     */
    public static UserException usernameExists() {
        return UserException.of(ErrorCode.USERNAME_EXISTS);
    }
    
    /**
     * 邮箱已存在异常
     * 
     * @return UserException实例
     */
    public static UserException emailExists() {
        return UserException.of(ErrorCode.EMAIL_EXISTS);
    }
    
    /**
     * 密码错误异常
     * 
     * @return UserException实例
     */
    public static UserException passwordError() {
        return UserException.of(ErrorCode.PASSWORD_ERROR);
    }
    
    /**
     * 用户已被禁用异常
     * 
     * @return UserException实例
     */
    public static UserException userDisabled() {
        return UserException.of(ErrorCode.USER_DISABLED);
    }
}