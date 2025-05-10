package vip.gpg123.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.setting.yaml.YamlUtil;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;
import io.fabric8.openshift.client.OpenShiftClient;
import org.springframework.beans.BeanUtils;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
/**
 * @author gaopuguang
 * @date 2025/3/1 2:34
 **/
public class K8sUtil {

    /**
     * 配置文件地址
     */
    private static final String configFile = "D:\\project\\cloud-server\\k8s\\config";
            //SystemUtil.getOsInfo().isWindows() ? "C:/Users/" + SystemUtil.getUserInfo().getName() + "/.kube/config" : "/root/.kube/config";

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
     * k8s客户端
     * @return r
     */
    public static KubernetesClient createKClient() {
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

    public static void main(String[] args) {
        Config config = getConfig();
        System.out.println(config);
    }
}

