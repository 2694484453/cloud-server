import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class DockerTest {

    private static final Boolean tls = true;

    private static final String certPath = "";

    private static final String address = "tcp://hongkong.gpg123.vip:2345";

    @Test
    public void test() {
        // 创建 Docker 客户端配置
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(address)
                .withDockerTlsVerify(false)
                .build();

        // 创建 HTTP 客户端
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(URI.create(address))
                .build();

        // 创建 Docker 客户端

        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build()) {
            // 测试连接并获取 Docker 信息
            System.out.println(dockerClient.infoCmd().exec());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭客户端
    }
}
