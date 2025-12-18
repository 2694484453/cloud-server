package vip.gpg123;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableRabbit
@MapperScan({
        "vip.gpg123.system.mapper",
        "vip.gpg123.quartz.mapper",
        "vip.gpg123.ai.mapper",
        "vip.gpg123.app.mapper",
        "vip.gpg123.caddy.mapper",
        "vip.gpg123.devops.mapper",
        "vip.gpg123.discovery.mapper",
        "vip.gpg123.docker.mapper",
        "vip.gpg123.domain.mapper",
        "vip.gpg123.git.mapper",
        "vip.gpg123.kubernetes.mapper",
        "vip.gpg123.nas.mapper",
        "vip.gpg123.prometheus.mapper",
        "vip.gpg123.repo.mapper",
        "vip.gpg123.scheduling.mapper",
        "vip.gpg123.tools.mapper",
        "vip.gpg123.tracing.mapper",
        "vip.gpg123.traefik.mapper",
        "vip.gpg123.vps.mapper",
        "vip.gpg123.wallpaper.mapper",
        "vip.gpg123.dashboard.mapper"
})
public class CloudServerApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SpringApplication.run(CloudServerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  cloud-server启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
