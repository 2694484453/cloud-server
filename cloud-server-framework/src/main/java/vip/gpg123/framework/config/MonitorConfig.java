package vip.gpg123.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class MonitorConfig {

    @Data
    @Component
    @ConfigurationProperties(prefix = "monitor.prometheus")
    public static class PrometheusProperties {

        private String path;

        private String url;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "monitor.alert-manager")
    public static class AlertManagerProperties {
        private String url;
    }
}
