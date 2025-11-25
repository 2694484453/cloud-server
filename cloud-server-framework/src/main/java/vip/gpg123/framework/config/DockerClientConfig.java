package vip.gpg123.framework.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class DockerClientConfig {

    @Value("${docker.address}")
    private String address;

    @Value("${docker.tls.enabled}")
    private boolean tlsEnabled;

    @Value("${docker.tls.certPath}")
    private String certPath;

    /**
     * dockerClient
     * @return r
     */
    @Bean
    public DockerClient dockerClient() {
        // 创建 Docker 客户端配置
        com.github.dockerjava.core.DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(address)
                .withDockerTlsVerify(tlsEnabled)
                .build();

        // 创建 HTTP 客户端
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(URI.create(address))
                .build();

        // 创建 Docker 客户端
        return DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }
}
