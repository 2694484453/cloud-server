package vip.gpg123.framework.config.domain;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/2 15:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NacosClient {

    private String api;

    private String username;

    private String password;

    /**
     * 指标状态
     */
    public String status(){
        HttpResponse httpResponse = HttpUtil.createGet(api + "/nacos/v1/ns/operator/metrics")
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        return jsonObject.get("status").toString();
    }

    /**
     * 命名空间
     *
     * @return r
     */
    public List<?> namespaceList() {
        HttpResponse httpResponse = HttpUtil.createGet(api + "/nacos/v1/console/namespaces")
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONArray data = JSONUtil.parseArray(jsonObject.get("data"));
        return Convert.toList(data);
    }

    /**
     * 服务列表
     * @return r
     */
    public List<?> serviceList() {
        List<Object> result = new ArrayList<>();
        // 获取全部ns
        List<?> namespaceList = namespaceList();
        namespaceList.forEach(e -> {
            JSONObject item = JSONUtil.parseObj(e);
            String ns = item.get("namespace").toString();
            List<?> sericeList = serviceList(ns);
            result.addAll(sericeList);
        });
        return result;
    }

    /**
     * 服务列表
     *
     * @return r
     */
    public List<?> serviceList(String namespace) {
        HttpResponse httpResponse = HttpUtil.createGet(api + "/nacos/v1/ns/catalog/services?hasIpCount=true&withInstances=false&pageNo=" + 1 + "&pageSize=9999&namespaceId=" + namespace)
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        List<Object> result = new ArrayList<>();
        JSONArray serviceListJsonArr = JSONUtil.parseArray(jsonObject.get("serviceList"));
        List<?> serviceList = Convert.toList(serviceListJsonArr);
        serviceList.forEach(e -> {
            JSONObject service = JSONUtil.parseObj(e);
            service.putOpt("namespace", namespace);
            result.add(service);
        });
        return result;
    }
}
