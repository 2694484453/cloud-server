package vip.gpg123.framework.config;

import vip.gpg123.common.utils.K8sUtil;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaopuguang
 * @date 2024/11/24 2:26
 **/
@Configuration
public class KubernetesClientConfig {

    @Bean(name = "KubernetesClient")
    public KubernetesClient kubernetesClient() {
        return K8sUtil.createKClient();
    }


    @Bean(name = "OpenShiftClient")
    public OpenShiftClient openShiftClient() {
        return K8sUtil.createOClient();
    }

    @Bean(name = "defaultConfigPath")
    public String defaultConfigPath() {
        return K8sUtil.defaultConfigFilePath();
    }
}
