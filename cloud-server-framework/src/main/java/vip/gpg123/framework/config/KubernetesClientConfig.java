package vip.gpg123.framework.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import vip.gpg123.common.utils.SecurityUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/11/24 2:26
 **/
@Configuration
public class KubernetesClientConfig {

    @Autowired
    private ApplicationContext context;

    /**
     * 配置文件地址
     */
    private static final String kubeConfig;

    /**
     * 默认context
     */
    private static final String defaultContext = "ark.gpg123.vip";


    // 静态代码块用于初始化静态变量
    static {
        // 获取系统下的目录
        kubeConfig = SystemUtil.getUserInfo().getHomeDir() + "/.kube/config";
        if (!FileUtil.exist(kubeConfig)) {
            throw new RuntimeException(kubeConfig + "不存在，请检查");
        }
    }



    // 根据当前用户获取客户端
    public KubernetesClient getClient() {
        String username = SecurityUtils.getUsername(); // 获取当前用户（需自行实现）
        return context.getBean(KubernetesClient.class, username);
    }

    /**
     * k8s-client
     * @return r
     */
    @Bean(name = "KubernetesClient")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)// 每次请求生成新实例
    public KubernetesClient kubernetesClient() {
        try {
            // 读取配置文件
            String content = FileUtil.readString(kubeConfig, StandardCharsets.UTF_8);
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
        return kubeConfig;
    }
}
