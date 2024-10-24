package com.ruoyi.web.controller.cluster;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/9/6 22:39
 **/
@RestController
@RequestMapping("/cluster")
@Api(value = "集群管理")
public class ClusterController {

    @Value("${kube.path}")
    private String configPath;

    /**
     * 集群列表
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    private AjaxResult list() {
        File[] files = FileUtil.ls(configPath);
        List<Config> clusters = new ArrayList<>();
        try {
            for (File value : files) {
                // 配置文件信息
                File file = FileUtil.file(value);
                Config config = KubeConfigUtils.parseConfig(file);
                clusters.add(config);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
