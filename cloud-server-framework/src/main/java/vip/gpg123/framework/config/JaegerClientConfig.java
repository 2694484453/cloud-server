package vip.gpg123.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.gpg123.framework.config.domain.JaegerClient;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2025/2/14 14:59
 */
@Configuration
public class JaegerClientConfig {

    @Value("${trace.api}")
    private String api;

    @Bean(name = "JaegerClient")
    public JaegerClient jaegerClient() {
        JaegerClient client = new JaegerClient();
        client.setEndpoint(api);
        return client;
    }
}
