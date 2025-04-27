package vip.gpg123.common.utils.helm;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.List;

public class HelmUtils {

    /**
     * helm list
     * @param namespace 命名空间
     * @return r
     */
    public static String listJsonStr(String namespace,String kubeContext) {
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
     * @param namespace 命名空间
     * @return r
     */
    public static JSONArray listJsonArray(String namespace,String kubeContext) {
        String jsonStr = listJsonStr(namespace,kubeContext);
        return JSONUtil.parseArray(jsonStr);
    }

    /**
     * helm list
     * @param namespace 命名空间
     * @return r
     */
    public static List<HelmApp> list(String namespace,String kubeContext) {
        JSONArray jsonArray = listJsonArray(namespace,kubeContext);
        return Convert.toList(HelmApp.class, jsonArray);
    }

    /**
     * helm install
     * @param namespace 命名空间
     * @param chartName chart名称
     * @param version v
     * @param kubeContext kubeContext
     */
    public static void install(String namespace, String repoName, String chartName, String version, String kubeContext) {
        namespace = StrUtil.isBlank(namespace) ? chartName : namespace;
        String[] init = new String[]{"helm", "install", chartName, repoName + "/" + chartName, "--namespace", namespace, "--kube-context", kubeContext, "--output", "json"};
        if (StrUtil.isNotBlank(version)) {
            init = ArrayUtil.append(init, "--version", version);
        }
        RuntimeUtil.exec(init);
    }

    /**
     * helm uninstall
     * @param namespace 命名空间
     * @param releaseName 发布名称
     * @param kubeContext kubeContext
     */
    public static void uninstall(String namespace, String releaseName, String kubeContext) {
        RuntimeUtil.exec("helm", "uninstall", releaseName, "--namespace", namespace, "--kube-context", kubeContext);
    }

    public static String getChartVersion(String chartName) {
        return chartName.split("-")[1];
    }

    public static String getChartName(String chartName) {
        return chartName.split("-")[0];
    }


}
