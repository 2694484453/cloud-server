package vip.gpg123.framework;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class UmamiConfig {

    /**
     * 用于读取配置文件中的 umami.* 属性
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "analytics.umami.cloud-web")
    public static class umamiCloudWebProperties {
        // 必须生成 Getter 和 Setter 方法
        private String websiteId;
    }

    /**
     * 用于读取配置文件中的 umami.* 属性
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "analytics.umami.wallpaper")
    public static class umamiWallpaperProperties {
        // 必须生成 Getter 和 Setter 方法
        private String websiteId;
    }
}
