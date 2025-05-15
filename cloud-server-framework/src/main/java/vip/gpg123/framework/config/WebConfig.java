package vip.gpg123.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure "global" cross-origin request processing. The configured CORS
     * mappings apply to annotated controllers, functional endpoints, and static
     * resources.
     * <p>Annotated controllers can further declare more fine-grained config via
     * {@link CrossOrigin @CrossOrigin}.
     * In such cases "global" CORS configuration declared here is
     * {@link CorsConfiguration#combine(CorsConfiguration) combined}
     * with local CORS configuration defined on a controller method.
     *
     * @param registry
     * @see CorsRegistry
     * @see CorsConfiguration#combine(CorsConfiguration)
     * @since 4.2
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 精确匹配前端地址[4,7](@ref)
                .allowedMethods("*") // 必须包含OPTIONS[4](@ref)
                .allowedHeaders("*")
                .allowCredentials(true) // 允许携带Cookie时需指定具体域名[4](@ref)
                .maxAge(3600); // 预检请求缓存时间（秒）
    }
}
