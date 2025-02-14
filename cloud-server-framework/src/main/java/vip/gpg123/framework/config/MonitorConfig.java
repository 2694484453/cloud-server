package vip.gpg123.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.gpg123.framework.config.domain.AlertManagerClient;
import vip.gpg123.framework.config.domain.PrometheusClient;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2025/2/14 14:29
 */
@Configuration
public class MonitorConfig {

    @Value("${monitor.prometheus.endpoint}")
    private String prometheusEndpoint;

    @Value("${monitor.alertmanager.endpoint}")
    private String alertManagerEndpoint;

    @Bean
    public PrometheusClient prometheusClient() {
        PrometheusClient client = new PrometheusClient();
        client.setEndpoint(prometheusEndpoint);
        return client;
    }

    @Bean
    public AlertManagerClient alertManagerClient() {
        AlertManagerClient client = new AlertManagerClient();
        client.setEndpoint(alertManagerEndpoint);
        return client;
    }
}
