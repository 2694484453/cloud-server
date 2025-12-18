package vip.gpg123.common.utils.helm;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.yaml.YamlUtil;
import cn.hutool.system.SystemUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HelmUtils {

    static String kubeConfig;

    // 静态代码块用于初始化静态变量
    static {
        // 获取系统下的目录
        kubeConfig = SystemUtil.getUserInfo().getHomeDir() + "/.kube/config";
        if (!FileUtil.exist(kubeConfig)) {
            throw new RuntimeException(kubeConfig + "不存在，请检查");
        }
    }

    /**
     * helm list
     *
     * @param namespace 命名空间
     * @return r
     */
    public static String listJsonStr(String namespace, String kubeContext) {
        String[] init = new String[]{"helm", "list", "--output", "json"};
        if (StrUtil.isBlank(namespace)) {
            init = ArrayUtil.append(init, "-A");
        } else {
            init = ArrayUtil.append(init, "--namespace ", namespace);
        }
        init = ArrayUtil.append(init, "--kube-context", kubeContext);
        return RuntimeUtil.execForStr(init);
    }

    /**
     * helm list json
     *
     * @param namespace 命名空间
     * @return r
     */
    public static JSONArray listJsonArray(String namespace, String kubeContext) {
        String jsonStr = listJsonStr(namespace, kubeContext);
        return JSONUtil.parseArray(jsonStr);
    }

    /**
     * helm list
     *
     * @param namespace 命名空间
     * @return r
     */
    public static List<HelmApp> list(String namespace, String kubeContext) {
        JSONArray jsonArray = listJsonArray(namespace, kubeContext);
        return Convert.toList(HelmApp.class, jsonArray);
    }

    /**
     * helm install
     *
     * @param namespace   命名空间
     * @param chartUrl    url
     * @param kubeContext kubeContext
     */
    public static String install(String releaseName, String namespace, String chartUrl, String valuesFilePath, String kubeContext, String kubeConfigPath) {
        String[] init = new String[]{"helm", "install"};
        if (StrUtil.isNotBlank(valuesFilePath)) {
            init = ArrayUtil.append(init, "-f", valuesFilePath);
        }
        if (StrUtil.isNotBlank(releaseName)) {
            init = ArrayUtil.append(init, releaseName);
        }
        if (StrUtil.isNotBlank(chartUrl)) {
            init = ArrayUtil.append(init, chartUrl);
        }
        // 命名空间
        if (StrUtil.isNotBlank(namespace)) {
            init = ArrayUtil.append(init, "--namespace", namespace);
        }
        // 名称
        if (StrUtil.isNotBlank(kubeContext)) {
            init = ArrayUtil.append(init, "--kube-context", kubeContext);
        }
        // 路径
        if (StrUtil.isNotBlank(kubeConfigPath)) {
            init = ArrayUtil.append(init, "--kubeconfig", kubeConfigPath);
        }
        init = ArrayUtil.append(init, "--output", "json");
        printCmd(init);
        String res = RuntimeUtil.execForStr(StandardCharsets.UTF_8, init);
        // 删除
        if(FileUtil.exist(valuesFilePath)) {
            FileUtil.del(valuesFilePath);
        }
        return res;
    }

    /**
     * 更新
     * @param releaseName rn
     * @param namespace ns
     * @param chartUrl c
     * @param values v
     * @param kubeContext k
     * @param kubeConfigPath k
     * @return r
     */
    public static String upgrade(String releaseName, String namespace, String chartUrl, String values, String kubeContext, String kubeConfigPath) {
        String[] init = new String[]{"helm", "upgrade"};
        if (StrUtil.isNotBlank(releaseName)) {
            init = ArrayUtil.append(init, releaseName);
        }
        printCmd(init);
        return RuntimeUtil.execForStr(StandardCharsets.UTF_8, init);
    }

    /**
     * helm uninstall
     *
     * @param namespace   命名空间
     * @param releaseName 发布名称
     * @param kubeContext kubeContext
     */
    public static String uninstall(String namespace, String releaseName, String kubeContext, String kubeConfigPath) {
        return RuntimeUtil.execForStr(StandardCharsets.UTF_8, "helm", "uninstall", releaseName, "--namespace", namespace, "--kube-context", kubeContext, "--kube-config", kubeConfigPath);
    }

    /**
     * 获取状态
     *
     * @param releaseName rn
     * @param kubeContext kc
     * @return r
     */
    public static HelmStatus status(String releaseName, String namespace, String kubeContext) {
        String json = RuntimeUtil.execForStr("helm", "status", releaseName, "--namespace", namespace, "--kube-context", kubeContext, "--output", "json");
        return JSONUtil.toBean(json, HelmStatus.class);
    }

    /**
     * 新增仓库
     *
     * @param repoName repo名称
     * @param repoUrl  地址
     */
    public static String repoAdd(String repoName, String repoUrl) {
        return RuntimeUtil.execForStr("helm", "repo", "add", repoName, repoUrl);
    }

    /**
     * 更新仓库
     *
     * @param repoName repo
     * @return r
     */
    public static String repoUpdate(String repoName) {
        return RuntimeUtil.execForStr("helm", "repo", "update", repoName);
    }

    /**
     * 删除仓库
     *
     * @param repoName repo
     * @return r
     */
    public static String repoRemove(String repoName) {
        return RuntimeUtil.execForStr("helm", "repo", "remove", repoName);
    }

    /**
     * 渲染参数
     *
     * @param chartUrl url
     * @return r
     */
    public static String showValues(String chartUrl) {
        String[] init = new String[]{"helm", "show", "values"};
        if (StrUtil.isNotBlank(chartUrl)) {
            init = ArrayUtil.append(init, chartUrl);
        }
        return RuntimeUtil.execForStr(init);
    }

    /**
     * 获取系统当前context
     *
     * @return r
     */
    public static String getCurrentContext() {
        return YamlUtil.loadByPath(kubeConfig, Map.class).get("current-context").toString();
    }

    /**
     * 集群信心
     *
     * @return r
     */
    public static List<Object> clusters() {
        Object o = YamlUtil.loadByPath(kubeConfig, Map.class).get("clusters");
        JSONArray jsonArray = JSONUtil.parseArray(o);
        return JSONUtil.toList(jsonArray, Object.class);
    }

    public static void printCmd(String[] cmd) {
        System.out.println(StrUtil.join(" ", (Object) cmd));
    }
}
