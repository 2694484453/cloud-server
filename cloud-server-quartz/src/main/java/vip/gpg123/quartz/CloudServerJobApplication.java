package vip.gpg123.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 任务中心
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CloudServerJobApplication {
    public static void main(String[] args) {

        SpringApplication.run(CloudServerJobApplication.class, args);
    }
}