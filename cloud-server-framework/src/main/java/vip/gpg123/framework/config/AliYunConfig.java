package vip.gpg123.framework.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AliYunConfig {

    /**
     * 用于读取配置文件中的 ali.oss.* 属性
     */
    @Data
    @Component
    @ConfigurationProperties(prefix = "ali.oss")
    public static class OssProperties {

        private String endpoint;

        private String bucketName;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "ali.auth")
    public static class AliAuthProperties {

        private String accessKeyId;

        private String accessKeySecret;

        private String aiApiKey;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "ali.ai")
    public static class AiProperties {

        private String apiKey;

        private String url;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "ali.ai-hui-hua")
    public static class AiHuiHuaProperties {

        private String apiKey;

        private String url;
    }


    /**
     * 创建 OSS Client Bean
     *
     * @return OSS 客户端实例
     */
    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(OssProperties ossProperties, AliAuthProperties ossAuthProperties) {
        // 1. 创建凭证提供者
        DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(ossAuthProperties.accessKeyId, ossAuthProperties.accessKeySecret);

        // 2. 使用 Builder 构建 OSS 客户端
        return OSSClientBuilder
                .create()
                .endpoint(ossProperties.endpoint)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    // 将配置类作为一个 Bean 注入，方便在其他地方获取配置
    @Bean
    public OssProperties ossProperties() {
        return new OssProperties();
    }
}
