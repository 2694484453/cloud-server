package com.ruoyi.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:48
 **/
public class K8sUtil {

    private static final String linuxConfigfile = "/root/.kube/config";

    private static final String windowsConfigFile = "C:/Users/"+ SystemUtil.getUserInfo().getName()+"/.kube/config";

    public static KubernetesClient createKClient() {
        try {
            String content = FileUtil.readString(linuxConfigfile, StandardCharsets.UTF_8);
            Config config = Config.fromKubeconfig(content);
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static KubernetesClient createClientWindows() {
        try {
            String content = FileUtil.readString(windowsConfigFile, StandardCharsets.UTF_8);
            Config config = Config.fromKubeconfig(content);
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OpenShiftClient createOClient(){
        KubernetesClient kubernetesClient = createKClient();
        return kubernetesClient.adapt(OpenShiftClient.class);
    }
}
