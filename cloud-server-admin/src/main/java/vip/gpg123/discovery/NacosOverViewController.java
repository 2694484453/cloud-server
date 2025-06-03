package vip.gpg123.discovery;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.discovery.domain.NacosNameSpace;
import vip.gpg123.discovery.service.NacosApi;
import vip.gpg123.discovery.service.NacosNameSpaceService;
import vip.gpg123.framework.config.domain.NacosClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/3/2 16:50
 **/
@RestController
@RequestMapping("/discovery/nacos")
@Api(tags = "【discovery】nacos服务概览")
public class NacosOverViewController extends BaseController {

    @Autowired
    private NacosClient nacosClient;

    @Autowired
    private NacosNameSpaceService nacosNameSpaceService;

    @Autowired
    private NacosApi nacosApi;

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    private AjaxResult overView() {
        String userName = getUsername();
        String nameSpaceId = userName.replaceAll("\\.","-");
        Map<String, Object> result = new HashMap<>();
        // 命名空间数量
        result.put("nameSpaceCount", nacosNameSpaceService.count(new LambdaQueryWrapper<NacosNameSpace>()
                .eq(NacosNameSpace::getCreateBy,  getUsername())
        ));
        // 服务数量
        result.put("serviceCount", nacosApi.service(nameSpaceId,1,9999).getCount());
        // 状态
        result.put("status", nacosApi.metrics().get("status"));
        return AjaxResult.success(result);
    }
}
