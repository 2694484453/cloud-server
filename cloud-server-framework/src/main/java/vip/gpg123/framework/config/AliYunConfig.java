package vip.gpg123.framework.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliYunConfig {

    @Value("${cloud.aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${cloud.aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${cloud.aliyun.endpoint}")
    private String endpoint;

    @Bean
    public StaticCredentialProvider staticCredentialsProvider() {
        return StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());
    }

    @Bean
    public DefaultCredentialProvider defaultCredentialProvider() {
        return new DefaultCredentialProvider(accessKeyId,accessKeyId);
    }


    /**
     * oss简单实例
     * @return r
     */
    @Bean
    public OSS ossClient() {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
