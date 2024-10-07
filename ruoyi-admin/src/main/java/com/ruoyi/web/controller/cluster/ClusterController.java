package com.ruoyi.web.controller.cluster;

import cn.hutool.core.convert.Convert;
import cn.hutool.setting.Setting;
import cn.hutool.setting.yaml.YamlUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2024/9/6 22:39
 **/
@RestController
@RequestMapping("/cluster")
@Api(value = "集群管理")
public class ClusterController {

    private static final String configPath = "/root/.kube/config";

    /**
     * 集群列表
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    private AjaxResult list() {
        Map map = YamlUtil.loadByPath(configPath, Map.class);
        List<Object> clusters = Convert.toList(Object.class, map.get("clusters"));
        return AjaxResult.success(clusters);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    private TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<Object> clusters = Convert.toList(Object.class, ajaxResult.get("data"));
        return PageUtils.toPage(clusters);
    }
}
