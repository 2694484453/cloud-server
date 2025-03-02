package vip.gpg123.discovery;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.framework.config.domain.NacosClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/3/2 16:50
 **/
@RestController
@RequestMapping("/discovery/nacos")
@Api(tags = "【discovery】nacos服务概览")
public class NacosOverViewController {

    @Autowired
    private NacosClient nacosClient;

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    private AjaxResult overView() {
        List<?> namespaceList = nacosClient.namespaceList();
        List<?> serviceList = nacosClient.serviceList();
        Map<String, Object> result = new HashMap<>();
        result.put("namespaceCount", namespaceList.size());
        result.put("serviceCount", serviceList.size());
        result.put("status", nacosClient.status());
        return AjaxResult.success(result);
    }
}
