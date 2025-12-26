package vip.gpg123.framework.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class DruidMonitorConfig {

    /**
     * 配置 Druid 监控页面 Servlet
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // IP 白名单（留空表示允许所有）
        servletRegistrationBean.addInitParameter("allow", "");
        // IP 黑名单（deny 优先于 allow）
        // servletRegistrationBean.addInitParameter("deny", "192.168.1.100");

        // 登录账号密码
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");

        // 是否可重置统计
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    /**
     * 配置 Web 监控的 Filter
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean =
                new FilterRegistrationBean<>(new WebStatFilter());

        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));

        // 忽略静态资源
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.css,*.png,*.jpg,*.gif,*.ico,/druid/*");

        // 监控 session
        filterRegistrationBean.addInitParameter("profileEnable", "true");

        return filterRegistrationBean;
    }
}