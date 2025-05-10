import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class DockerTest {

    private String address = "tcp://110.42.40.8:2375";

    @Test
    public void test() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                // 地址
                .withDockerHost(address)
                // 不需要认证
                .withDockerTlsVerify(false)
                .withRegistryUsername("root")
                .withRegistryPassword("1999y4m30D")
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        System.out.println(dockerClient.infoCmd().exec());
    }
}
