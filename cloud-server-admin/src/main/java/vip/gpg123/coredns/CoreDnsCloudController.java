package vip.gpg123.coredns;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/10/9 1:34
 **/
@RestController
@RequestMapping("/corednsCloud")
public class CoreDnsCloudController {

    @Qualifier("KubernetesClient")
    @Autowired
    private KubernetesClient kubernetesClient;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list() {
        List<?> list = getConfigMaps();
        return AjaxResult.success(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page() {
        List<?> list = getConfigMaps();
        return PageUtils.toPage(list);
    }

    private List<?> getConfigMaps() {
        ConfigMap configMap = kubernetesClient.configMaps().inNamespace("kube-system").withName("coredns-custom").get();
        Map<String, String> map = configMap.getData();
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        map.forEach((k, v) -> {
            mapList.add(new HashMap<String, Object>() {{
                put("key", k);
                put("value", v);
                put("namespace", "kube-system");
                put("name", "coredns-custom");
                put("kind", configMap.getKind());
                put("createTime", configMap.getMetadata().getCreationTimestamp());
            }});
        });
        return mapList;
    }
}
