package vip.gpg123.dns;

import cn.hutool.core.convert.Convert;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/11/26 1:38
 **/
@RestController
@Api(tags = "coredns控制")
public class CoreDnsController {

    @Qualifier("KubernetesClient")
    @Autowired
    private KubernetesClient kubernetesClient;

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            ConfigMap configMap = kubernetesClient.configMaps().inNamespace("kube-system").withName("coredns-custom").get();
            Map<String, String> map = configMap.getData();
            map.forEach((k, v) -> {
                list.add(new HashMap<String, String>() {{
                    put("key", k);
                    put("value", v);
                }});
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success("操作成功", list);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> list = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(list);
    }

}
