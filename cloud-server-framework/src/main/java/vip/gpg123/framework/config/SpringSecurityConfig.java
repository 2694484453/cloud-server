package vip.gpg123.framework.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Security配置
 */
@Configuration
public class SpringSecurityConfig {

    /**
     * 项目启动时，初始化配置：设置安全上下文（SecurityContext）的存储策略
     */
    @PostConstruct
    public void setStrategyName(){
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);// 可继承的安全上下文
    }

    @Bean
    public InitializingBean initializingBean() {
        return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}
