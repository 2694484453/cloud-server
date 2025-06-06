package vip.gpg123.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.gpg123.framework.client.NacosClient;

@Configuration
public class NacosConfig {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    @Value("${spring.cloud.nacos.config.group}")
    private String group;

    @Value("${spring.cloud.nacos.config.username}")
    private String userName;

    @Value("${spring.cloud.nacos.config.password}")
    private String passWord;

    @Bean
    public NacosClient nacosClient() {
        return new NacosClient(serverAddr, namespace, group, userName, passWord);
    }
}
