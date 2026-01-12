package vip.gpg123.ai.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import vip.gpg123.framework.config.AliYunConfig;

public class StableDiffusionRequestConfig {

    @Bean
    public RequestInterceptor stableDiffusionRequestInterceptor(AliYunConfig.AliAuthProperties aliAuthProperties) {
        return requestTemplate -> {
            // 添加 Bearer Token（根据 Umami 要求）
            requestTemplate.header("Authorization", "Bearer " + aliAuthProperties.getAiApiKey());
            requestTemplate.header("Content-Type", "application/json");
        };
    }

}
