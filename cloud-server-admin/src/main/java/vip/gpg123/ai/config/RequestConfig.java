package vip.gpg123.ai.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RequestConfig {

    @Value("${analytics.umami.token}")
    private String umamiToken;

    @Bean
    public RequestInterceptor umamiRequestInterceptor() {
        return requestTemplate -> {
            // 添加 Bearer Token（根据 Umami 要求）
            requestTemplate.header("Authorization", "Bearer " + umamiToken);
            requestTemplate.header("Content-Type", "application/json;charset=UTF-8");
            requestTemplate.header("Accept", "application/json;charset=UTF-8");
            requestTemplate.header("User-Agent", "Mozilla/5.0");
            // 也可以加其他 header，比如 Content-Type 等
            // requestTemplate.header("X-Custom-Header", "value");
        };
    }
}
