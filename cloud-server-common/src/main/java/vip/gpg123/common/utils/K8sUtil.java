package vip.gpg123.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

import java.io.File;
import java.nio.charset.StandardCharsets;
/**
 * @author gaopuguang
 * @date 2025/3/1 2:34
 **/
public class K8sUtil {

    /**
     * 配置文件地址
     */
    private static final String configFile;

    static {
        configFile = SystemUtil.getUserInfo().getHomeDir() + "/.kube/config";
    }

    /**
     * 获取配置对象
     * @return r
     */
    public static Config getConfig() {
        try {
            String content = FileUtil.readString(configFile, StandardCharsets.UTF_8);
            return Config.fromKubeconfig(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * k8s客户端默认模式
     * @return r
     */
    public static KubernetesClient createDefaultKubernetesClient() {
        try {
            Config config = getConfig();
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上下文模式
     * @param context c
     * @return r
     */
    public static KubernetesClient createKubernetesClient(String context) {
        try {
            // 设置上下文
            NamedContext namedContext = new NamedContext();
            namedContext.setName(context);
            Config config = Config.fromKubeconfig(FileUtil.readString(configFile, StandardCharsets.UTF_8));
            config.setCurrentContext(namedContext);
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * openshift客户端
     * @return r
     */
    public static OpenShiftClient createDefaultOpenShiftClient() {
        KubernetesClient kubernetesClient = createDefaultKubernetesClient();
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    /**
     * openshift客户端
     * @param context c
     * @return r
     */
    public static OpenShiftClient createOpenShiftClient(String context) {
        KubernetesClient kubernetesClient = createKubernetesClient(context);
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    /**
     * 解析文件
     */
    public static Config parseConfig(String content) {
        return Config.fromKubeconfig(content);
    }

    /**
     * 从文件解析
     * @param file 文件
     * @return r
     */
    public static Config parseConfig(File file) {
        String content = FileUtil.readString(file, StandardCharsets.UTF_8);
        return Config.fromKubeconfig(content);
    }

    /**
     * 默认路径
     * @return r
     */
    public static String defaultConfigFilePath() {
        return configFile;
    }


    public static void main(String[] args) {
        Config config = getConfig();
        System.out.println(config);
    }
}

