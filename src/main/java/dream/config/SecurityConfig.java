package dream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security安全配置类
 * 
 * 这个配置类的作用：
 * 1. 配置Spring Security的安全策略
 * 2. 提供密码加密器Bean供其他组件使用
 * 3. 暂时禁用Spring Security的默认安全机制，方便开发测试
 * 
 * 技术解释：
 * - @Configuration：标记这是一个配置类，Spring会在启动时加载
 * - @EnableWebSecurity：启用Spring Security的Web安全功能
 * - WebSecurityConfigurerAdapter：Spring Security的配置适配器基类
 * - BCryptPasswordEncoder：业界标准的密码加密算法，安全性高
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    /**
     * 密码加密器Bean
     * 
     * 技术解释：
     * - @Bean注解告诉Spring这是一个需要管理的组件
     * - BCrypt是一种慢哈希函数，专门设计用于密码加密
     * - 它会自动加盐(salt)，每次加密同样的密码都会产生不同的结果
     * - 这样可以防止彩虹表攻击和暴力破解
     * 
     * @return BCryptPasswordEncoder实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * HTTP安全配置
     * 
     * 技术解释：
     * - 这个方法配置了哪些URL需要认证，哪些可以匿名访问
     * - 目前为了开发方便，我们暂时禁用了所有安全检查
     * - 在生产环境中，需要根据实际需求配置具体的安全策略
     * 
     * @param http HttpSecurity配置对象
     * @throws Exception 配置异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（跨站请求伪造保护）
            // 在开发阶段暂时禁用，生产环境需要根据情况启用
            .csrf().disable()
            
            // 配置请求授权
            .authorizeRequests()
                // 允许所有请求无需认证（开发阶段配置）
                .anyRequest().permitAll()
            .and()
            
            // 禁用HTTP Basic认证
            .httpBasic().disable()
            
            // 禁用表单登录
            .formLogin().disable();
    }
}