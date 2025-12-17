package vip.gpg123.nas.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Base64;

public class FrpServerFeignConfig {

    @Value("${frp.server.userName}")
    private String userName;

    @Value("${frp.server.passWord}")
    private String passWord;

    @Bean
    public RequestInterceptor umamiRequestInterceptor() {
        String auth = userName + ":" + passWord;
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
        };
    }
}
