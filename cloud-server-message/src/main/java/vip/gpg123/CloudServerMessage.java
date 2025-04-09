package vip.gpg123;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 消息中心
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CloudServerMessage {
    public static void main(String[] args) {

        SpringApplication.run(CloudServerMessage.class, args);
    }
}