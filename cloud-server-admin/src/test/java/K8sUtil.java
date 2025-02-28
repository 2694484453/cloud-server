package vip.gpg123.common.utils;

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
     * k8s客户端
     * @return r
     */
    public static KubernetesClient createKClient() {
   
    }

    /**
     * openshift客户端
     * @return r
     */
    public static OpenShiftClient createOClient() {

    }

    public static String defaultConfigFilePath() {
        return configFile;
    }
}
