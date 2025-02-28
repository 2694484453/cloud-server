package vip.gpg123.framework.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/11/24 2:26
 **/
@Configuration
public class KubernetesClientConfig {

    /**
     * 配置文件地址
     */
    private static final String configFile = SystemUtil.getOsInfo().isWindows() ? "C:/Users/" + SystemUtil.getUserInfo().getName() + "/.kube/config" : "/root/.kube/config";


    @Bean(name = "KubernetesClient")
    public KubernetesClient kubernetesClient() {
        try {
            String content = FileUtil.readString(configFile, StandardCharsets.UTF_8);
            Config config = Config.fromKubeconfig(content);
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Bean(name = "OpenShiftClient")
    public OpenShiftClient openShiftClient(@Qualifier("KubernetesClient") KubernetesClient kubernetesClient) {
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    @Bean(name = "defaultConfigPath")
    public String defaultConfigPath() {
        return configFile;
    }
}
