package vip.gpg123.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class EmailConfig {

    @Data
    @Component
    @ConfigurationProperties(prefix = "email")
    public static class emailProperties {
        private String codeContent;
    }
}
