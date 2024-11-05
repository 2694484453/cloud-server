package com.ruoyi.web.controller.monitor;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRule;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleList;
import io.fabric8.openshift.client.OpenShiftClient;
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
public class PrometheusCloudController {

    @GetMapping("/list")
    public AjaxResult list() {
        OpenShiftClient openShiftClient = K8sUtil.createOClient();
        PrometheusRuleList prometheusRuleList = openShiftClient.monitoring().prometheusRules().inAnyNamespace().list();
        List<PrometheusRule> prometheusRules = prometheusRuleList.getItems();
        return AjaxResult.success(prometheusRules);
    }

    @GetMapping("/page")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<PrometheusRule> prometheusRules = Convert.toList(PrometheusRule.class, ajaxResult.get("data"));
        return PageUtils.toPage(prometheusRules);
    }
}
