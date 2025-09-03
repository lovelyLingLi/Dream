package dream.exception;


public class BaseException extends RuntimeException {
    
    /**
     * 错误码 - 用于标识具体的错误类型
     */
    private String errorCode;
    
    /**
     * 错误消息 - 用于描述错误的详细信息
     */
    private String errorMessage;
    
    /**
     * 默认构造函数
     */
    public BaseException() {
        super();
    }
    
    /**
     * 带错误消息的构造函数
     * 
     * @param message 错误消息
     */
    public BaseException(String message) {
        super(message);
        this.errorMessage = message;
    }
    
    /**
     * 带错误码和错误消息的构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
    
    /**
     * 带异常原因的构造函数
     * 
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }
    
    /**
     * 完整的构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * 设置错误码
     * 
     * @param errorCode 错误码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误消息
     * 
     * @return 错误消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 设置错误消息
     * 
     * @param errorMessage 错误消息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * 重写toString方法，提供更详细的异常信息
     * 
     * @return 异常的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        if (errorCode != null) {
            sb.append("[errorCode=").append(errorCode).append("]");
        }
        if (errorMessage != null) {
            sb.append(": ").append(errorMessage);
        }
        return sb.toString();
    }
}