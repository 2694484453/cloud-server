package vip.gpg123.framework.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/11/24 2:26
 **/
@Component
public class KubernetesClientConfig {

    /**
     * 配置文件地址
     */
    private static String configFile = "";

    /**
     * 默认context
     */
    private static final String defaultContext = "ark.gpg123.vip";

    /**
     * 默认构造器
     */
    public KubernetesClientConfig() {
        String osName = SystemUtil.getOsInfo().getName();
        switch (osName) {
            case "Windows":
                 configFile = "C:/Users/" + SystemUtil.getUserInfo().getName() + "/.kube/config";
                break;
            case "Linux":
                configFile =  "/root/.kube/config";
                break;
            case "Mac OS X":
                configFile = "/Users/"+SystemUtil.getUserInfo().getName() + "/.kube/config";
                break;
        }
    }

    /**
     * k8s-client
     * @return r
     */
    @Bean(name = "KubernetesClient")
    public KubernetesClient kubernetesClient() {
        try {
            // 读取配置文件
            String content = FileUtil.readString(configFile, StandardCharsets.UTF_8);
            Config config = Config.fromKubeconfig(content);
            // 设置一个context
            NamedContext namedContext = new NamedContext();
            namedContext.setName(defaultContext);
            config.setCurrentContext(namedContext);
            // 返回客户端
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * open client
     * @param kubernetesClient k
     * @return r
     */
    @Bean(name = "OpenShiftClient")
    public OpenShiftClient openShiftClient(@Qualifier("KubernetesClient") KubernetesClient kubernetesClient) {
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    /**
     * 配置文件
     * @return r
     */
    public static String defaultConfigPath() {
        return configFile;
    }
}
