package com.ruoyi.web.controller.coredns;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.K8sUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.ConfigMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:34
 **/
@RestController
@RequestMapping("/corednsCloud")
public class CoreDnsCloudController {

    @GetMapping("/list")
    public AjaxResult list() {
        ConfigMap configMap = K8sUtil.createKClient().configMaps().inNamespace("kube-system").withName("coredns-custom").get();
        Map<String, String> map = configMap.getData();
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        map.forEach((k, v) -> {
            mapList.add(new HashMap<String, Object>() {{
                put("id",i);
                put("key", k);
                put("value",v);
            }});
            i.getAndIncrement();
        });
        return AjaxResult.success(mapList);
    }

    @GetMapping("/page")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List mapList = Convert.convert(List.class, ajaxResult.get("data"));
        return PageUtils.toPage(mapList);
    }
}
