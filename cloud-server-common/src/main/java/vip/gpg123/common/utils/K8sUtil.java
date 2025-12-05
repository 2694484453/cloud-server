package vip.gpg123.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import io.fabric8.kubernetes.api.model.NamedContext;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.openshift.client.OpenShiftClient;

import java.io.File;
import java.io.IOException;
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
     *
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

    public static Config getConfig(String currentContext) {
        try {
            String content = FileUtil.readString(configFile, StandardCharsets.UTF_8);
            return Config.fromKubeconfig(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从文件获取对象
     *
     * @param configFilePath path
     * @return r
     * @throws IOException e
     */
    public static io.fabric8.kubernetes.api.model.Config getConfigFromFile(String configFilePath) throws IOException {
        return KubeConfigUtils.parseConfigFromString(FileUtil.readUtf8String(configFilePath));
    }

    /**
     * 从字符串获取对象
     *
     * @param content c
     * @return r
     * @throws IOException e
     */
    public static io.fabric8.kubernetes.api.model.Config getConfigFromStr(String content) throws IOException {
        return KubeConfigUtils.parseConfigFromString(content);
    }

    /**
     * 获取默认配置
     *
     * @return r
     * @throws IOException e
     */
    public static io.fabric8.kubernetes.api.model.Config getDefaultConfig() throws IOException {
        return KubeConfigUtils.parseConfigFromString(FileUtil.readUtf8String(configFile));
    }

    /**
     * 把对象转换为配置字符串
     *
     * @param config c
     * @return r
     * @throws IOException e
     */
    public static String configBeanToStr(Config config) throws IOException {
        return Serialization.yamlMapper().writeValueAsString(config);
    }

    /**
     * k8s客户端默认模式
     *
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
     * 传入currentContext
     *
     * @param currentContext c
     * @return r
     */
    public static KubernetesClient createKubernetesClient(String currentContext) {
        try {
            // 设置上下文
            Config config = Config.fromKubeconfig(currentContext, FileUtil.readString(configFile, StandardCharsets.UTF_8), defaultConfigFilePath());
            return new KubernetesClientBuilder()
                    .withConfig(config)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * openshift客户端
     *
     * @return r
     */
    public static OpenShiftClient createDefaultOpenShiftClient() {
        KubernetesClient kubernetesClient = createDefaultKubernetesClient();
        return kubernetesClient.adapt(OpenShiftClient.class);
    }

    /**
     * openshift客户端
     *
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
     *
     * @param file 文件
     * @return r
     */
    public static Config parseConfig(File file) {
        String content = FileUtil.readString(file, StandardCharsets.UTF_8);
        return Config.fromKubeconfig(content);
    }

    /**
     * 导出文件
     */
    public static void exportToFile(io.fabric8.kubernetes.api.model.Config kubeConfig, String exportPath) throws IOException {
        KubeConfigUtils.persistKubeConfigIntoFile(kubeConfig, exportPath);
    }

    /**
     * 默认路径
     *
     * @return r
     */
    public static String defaultConfigFilePath() {
        return configFile;
    }

    /**
     * 检查资源名称
     * @param resourceName r
     * @return r
     */
    public static boolean checkResourceName(String resourceName) {
        return KubernetesResourceUtil.isValidName(resourceName);
    }
}

