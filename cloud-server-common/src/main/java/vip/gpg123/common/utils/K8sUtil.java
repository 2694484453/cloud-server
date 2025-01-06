package com.ruoyi.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:48
 **/
public class K8sUtil {

    /**
     * 配置文件地址
     */
    private static final String configFile = SystemUtil.getOsInfo().isWindows() ? "C:/Users/" + SystemUtil.getUserInfo().getName() + "/.kube/config" : "/root/.kube/config";

    /**
     * k8s客户端
     * @return r
     */
    public static KubernetesClient createKClient() {
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

    /**
     * openshift客户端
     * @return r
     */
    public static OpenShiftClient createOClient() {
        KubernetesClient kubernetesClient = createKClient();
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    public static String defaultConfigFilePath() {
        return configFile;
    }
}
