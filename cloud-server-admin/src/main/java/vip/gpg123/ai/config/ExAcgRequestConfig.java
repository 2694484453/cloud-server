package vip.gpg123.ai.config;

import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import vip.gpg123.framework.config.AliYunConfig;

public class ExAcgRequestConfig {

    @Bean
    public RequestInterceptor exacgRequestInterceptor(AliYunConfig.AiHuiHuaProperties aiHuiHuaProperties) {
        return requestTemplate -> {
            // 添加 Bearer Token（根据 Umami 要求）
            requestTemplate.header("Authorization", "Bearer " + aiHuiHuaProperties.getApiKey());
            requestTemplate.header("Content-Type", "application/json");
            // 1. 添加 User-Agent，模拟 Chrome 浏览器
            requestTemplate.header("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
            // 2. 添加 Accept，表示能接收的内容类型
            requestTemplate.header("Accept", "*/*");
            requestTemplate.header("Host", "sd.exacg.cc");
            requestTemplate.header("Connection", "Keep-Alive");
        };
    }

    @Bean
    public Request.Options options() {
        // connectTimeout, readTimeout (单位：毫秒)
        return new Request.Options(5000, 40000);
    }
}
