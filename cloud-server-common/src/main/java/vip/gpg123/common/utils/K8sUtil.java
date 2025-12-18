package vip.gpg123.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.openshift.client.OpenShiftClient;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2025/3/1 2:34
 **/
public class K8sUtil {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();

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
     * 根据config对象，导出文件
     *
     * @param kubeConfig c
     * @param exportPath e
     * @throws IOException e
     */
    public static void exportToFile(io.fabric8.kubernetes.api.model.Config kubeConfig, String exportPath) throws IOException {
        KubeConfigUtils.persistKubeConfigIntoFile(kubeConfig, exportPath);
    }

    /**
     * 根据content内容导出到文件
     *
     * @param content    c
     * @param exportPath e
     * @throws IOException i
     */
    public static void exportToFile(String content, String exportPath) throws IOException {
        FileUtil.writeString(content, exportPath, StandardCharsets.UTF_8);
    }

    /**
     * 根据string内容导出到文件并返回
     *
     * @param content c
     * @return r
     */
    public static File exportConfigToTempFile(String content) {
        File file = FileUtil.createTempFile();
        FileUtil.writeString(content, file.getAbsolutePath(), StandardCharsets.UTF_8);
        return file;
    }

    /**
     * 生成参数文件
     * @param values v
     * @return r
     */
    public static File exportValuesToTempFile(String values) {
        File file = FileUtil.createTempFile();
        // 判断字符串中的内容类型
        String type = detectFormat(values);
        switch (type) {
            case "yaml":
                System.out.println("参数为yaml格式，不需要转换");
                FileUtil.writeString(values, file.getAbsolutePath(), StandardCharsets.UTF_8);
                break;
            case "json":
                System.out.println("参数为json格式，需要转换为yaml");
                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    JSONObject jsonObject = JSONUtil.parseObj(values);
                    YamlUtil.dump(jsonObject, fileWriter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "unknown":
                System.out.println("未知类型，防止错误生成空内容");
                values = "";
                FileUtil.writeString(values, file.getAbsolutePath(), StandardCharsets.UTF_8);
                break;
        }
        return file;
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
     *
     * @param resourceName r
     * @return r
     */
    public static boolean checkResourceName(String resourceName) {
        return KubernetesResourceUtil.isValidName(resourceName);
    }

    /**
     * 判断字符串是 JSON、YAML 还是都不是
     *
     * @param input 输入字符串
     * @return "json" / "yaml" / "unknown"
     */
    public static String detectFormat(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "unknown";
        }
        // 先尝试解析为 JSON（更严格，避免 YAML 误判简单 JSON）
        try {
            JsonNode node = JSON_MAPPER.readTree(input.trim());
            // 确保不是单个字符串或数字（如 "hello" 是合法 JSON 字符串，但可能是 YAML 内容）
            // 可选：要求必须是对象或数组
            if (node.isObject() || node.isArray()) {
                return "json";
            }
        } catch (Exception e) {
            // JSON 解析失败，继续尝试 YAML
        }

        // 尝试解析为 YAML
        try {
            Object obj = YAML_MAPPER.readValue(input, Object.class);
            // YAML 成功解析（即使是一个标量，也认为是 YAML）
            return "yaml";
        } catch (Exception e) {
            // YAML 也失败
        }
        return "unknown";
    }
}

