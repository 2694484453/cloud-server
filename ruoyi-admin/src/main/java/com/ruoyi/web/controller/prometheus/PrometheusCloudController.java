package com.ruoyi.web.controller.prometheus;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRule;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleList;
import io.fabric8.openshift.client.OpenShiftClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/10/15 0:58
 **/
@RestController
@RequestMapping("/monitorCloud")
@Api(tags = "监控中心云原生版")
public class PrometheusCloudController {

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        OpenShiftClient openShiftClient = K8sUtil.createOClient();
        List<PrometheusRule> prometheusRules;
        try {
            PrometheusRuleList prometheusRuleList = openShiftClient.monitoring().prometheusRules().inAnyNamespace().list();
            prometheusRules = prometheusRuleList.getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(prometheusRules);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> prometheusRules = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(prometheusRules);
    }
}
