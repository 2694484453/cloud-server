package com.ruoyi.common;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:48
 **/
public class K8sUtil {

    private static final String master = "http://124.221.2.29:8002";

    public static KubernetesClient createKClient() {
        try {
            Config config = new ConfigBuilder()
                    .withApiVersion("v1")
                    .withMasterUrl(master)
                    .build();
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
