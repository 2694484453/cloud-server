package vip.gpg123.common.utils.helm;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;

import java.util.List;

public class HelmUtils {

    static String kubeConfig;

    // 静态代码块用于初始化静态变量
    static {
        // 获取系统下的目录
        kubeConfig = SystemUtil.getUserInfo().getHomeDir() + "/.kube/config";
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
     * @param chartName   chart名称
     * @param version     v
     * @param kubeContext kubeContext
     */
    public static void install(String namespace, String repoName, String chartName, String version, String kubeContext) {
        String[] init = new String[]{"helm", "install"};
        if (StrUtil.isNotBlank(repoName) && StrUtil.isNotBlank(chartName)) {
            init = ArrayUtil.append(init, repoName + "/" + chartName);
        }
        // 命名空间
        if (StrUtil.isNotBlank(namespace)) {
            init = ArrayUtil.append(init, "--namespace", namespace);
        }
        if (StrUtil.isNotBlank(kubeContext)) {
            init = ArrayUtil.append(init, "--kube-context", kubeContext);
        }
        if (StrUtil.isNotBlank(version)) {
            init = ArrayUtil.append(init, "--version", version);
        }
        init = ArrayUtil.append(init, "--kube-config", kubeConfig, "--output", "json");
        RuntimeUtil.exec(init);
    }

    /**
     * helm uninstall
     *
     * @param namespace   命名空间
     * @param releaseName 发布名称
     * @param kubeContext kubeContext
     */
    public static void uninstall(String namespace, String releaseName, String kubeContext) {
        RuntimeUtil.exec("helm", "uninstall", releaseName, "--namespace", namespace, "--kube-context", kubeContext);
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

    public static String getChartVersion(String chartName) {
        return chartName.split("-")[1];
    }

    public static String getChartName(String chartName) {
        return chartName.split("-")[0];
    }


}
