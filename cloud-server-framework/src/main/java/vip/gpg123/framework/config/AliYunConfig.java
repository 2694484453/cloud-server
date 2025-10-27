package vip.gpg123.framework.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliYunConfig {

    @Value("${cloud.aliyun.accessKeyId}")
    private static String accessKeyId;

    @Value("${cloud.aliyun.accessKeySecret}")
    private static String accessKeySecret;

    @Bean
    public StaticCredentialProvider staticCredentialsProvider() {
        return StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());
    }

    @Bean
    public DefaultCredentialProvider defaultCredentialProvider() {
        return CredentialsProviderFactory.newDefaultCredentialProvider(accessKeyId,accessKeyId);
    }

//    @Bean(name = "AsyncDomainClient")
//    public AsyncClient createClient() {
//        return AsyncClient.builder()
//                .region("cn-hangzhou") // Region ID
//                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
//                .credentialsProvider(staticCredentialsProvider())
//                //.serviceConfiguration(Configuration.create()) // Service-level configuration
//                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
//                .overrideConfiguration(
//                        ClientOverrideConfiguration.create()
//                                // Endpoint 请参考 https://api.aliyun.com/product/Alidns
//                                .setEndpointOverride("alidns.cn-hangzhou.aliyuncs.com")
//                        //.setConnectTimeout(Duration.ofSeconds(30))
//                ).build();
//    }

    @Bean
    public OSS ossClient() throws ClientException {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "examplebucket";
        // 指定前缀，例如exampledir/object。
        String keyPrefix = "exampledir/object";
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-hangzhou";

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(defaultCredentialProvider())
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }
}
