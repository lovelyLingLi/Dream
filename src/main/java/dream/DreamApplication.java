package dream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Dream个人博客系统后端应用启动类
 * 基于Spring Boot 2.6.13构建
 * 提供RESTful API服务
 */
@SpringBootApplication
@MapperScan("dream.mapper")
public class DreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamApplication.class, args);
        System.out.println("\n=== Dream个人博客系统后端启动成功 ===");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API文档: http://localhost:8080/swagger-ui.html (待配置)");
        System.out.println("数据库: MySQL 8.0 - dream_db");
        System.out.println("技术栈: Spring Boot + MyBatis-Plus + MySQL");
        System.out.println("========================================\n");
    }

}