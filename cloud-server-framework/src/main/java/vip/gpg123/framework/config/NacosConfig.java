package vip.gpg123.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.gpg123.framework.config.domain.NacosClient;

/**
 * @author gaopuguang
 * @date 2025/3/2 15:49
 **/
@Configuration
public class NacosConfig {

    private static final String api = "http://hcs.gpg123.vip:8848";

    private static final String username = "";

    private static final String password = "";

    @Bean
    public NacosClient nacosClient() {
        return new NacosClient(api, username, password);
    }
}
