package vip.gpg123.repo.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import vip.gpg123.repo.domain.HelmApp;

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

    public static String getChartVersion(String chartName) {
        return chartName.split("-")[1];
    }

    public static String getChartName(String chartName) {
        return chartName.split("-")[0];
    }

}
