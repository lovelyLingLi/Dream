package dream.exception;

import dream.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    
    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理基础异常
     * 
     * @param e 基础异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<Object>> handleBaseException(BaseException e) {
        logger.warn("基础异常: {}", e.getMessage(), e);
        
        ErrorCode errorCode = ErrorCode.getByCode(e.getErrorCode());
        if (errorCode != null) {
            Result<Object> result = Result.error(errorCode, e.getErrorMessage());
            return ResponseEntity.status(errorCode.getHttpStatus()).body(result);
        } else {
            Result<Object> result = Result.error(ErrorCode.INTERNAL_ERROR, e.getErrorMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    
    /**
     * 处理参数验证异常（@RequestBody参数验证失败）
     * 
     * @param e 方法参数验证异常
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("参数验证异常: {}", e.getMessage());
        
        // 获取第一个验证失败的字段错误信息
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "参数验证失败";
        
        Result<Object> result = Result.error(ErrorCode.VALIDATION_ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    /**
     * 处理参数绑定异常（@ModelAttribute参数验证失败）
     * 
     * @param e 绑定异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Object>> handleBindException(BindException e) {
        logger.warn("参数绑定异常: {}", e.getMessage());
        
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        
        Result<Object> result = Result.error(ErrorCode.VALIDATION_ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    /**
     * 处理约束违反异常（@Validated参数验证失败）
     * 
     * @param e 约束违反异常
     * @return 统一响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.warn("约束违反异常: {}", e.getMessage());
        
        // 获取第一个违反约束的错误信息
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.isEmpty() ? "约束验证失败" : 
                             violations.iterator().next().getMessage();
        
        Result<Object> result = Result.error(ErrorCode.VALIDATION_ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    /**
     * 处理非法参数异常
     * 
     * @param e 非法参数异常
     * @return 统一响应结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("非法参数异常: {}", e.getMessage(), e);
        
        Result<Object> result = Result.error(ErrorCode.PARAM_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    /**
     * 处理运行时异常
     * 
     * @param e 运行时异常
     * @return 统一响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<Object>> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: {}", e.getMessage(), e);
        
        Result<Object> result = Result.error(ErrorCode.INTERNAL_ERROR, "系统内部错误，请稍后重试");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    
    /**
     * 处理其他所有异常
     * 
     * @param e 异常
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Object>> handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage(), e);
        
        Result<Object> result = Result.error(ErrorCode.INTERNAL_ERROR, "系统异常，请联系管理员");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}