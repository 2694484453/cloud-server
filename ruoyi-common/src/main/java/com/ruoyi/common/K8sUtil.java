package com.ruoyi.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import io.fabric8.kubernetes.client.Client;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;

import java.net.URL;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:48
 **/
public class K8sUtil {

    private static final String path = "config/k3s.gpg123.vip";

    public static Client createClient() {
        URL url = ResourceUtil.getResource(path);
        try {
            io.fabric8.kubernetes.api.model.Config configFile = KubeConfigUtils.parseConfig(FileUtil.file(url));
            io.fabric8.kubernetes.client.Config config = new Config();
            config.setApiVersion(configFile.getApiVersion());
            config.setMasterUrl(configFile.getClusters().get(0).getCluster().getServer());
            // config.setCaCertData(configFile.getClusters().get(0).getCluster().getCertificateAuthorityData());
            // config.setClientCertData(configFile.getUsers().get(0).getUser().getClientCertificateData());
            //config.setClientKeyData(configFile.getUsers().get(0).getUser().getClientKeyData());
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
