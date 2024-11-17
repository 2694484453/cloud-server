package com.ruoyi.web.controller.prometheus;

import cn.hutool.core.convert.Convert;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.K8sUtil;
import com.ruoyi.common.utils.PageUtils;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRule;
import io.fabric8.openshift.api.model.monitoring.v1.PrometheusRuleList;
import io.fabric8.openshift.api.model.monitoring.v1alpha1.AlertmanagerConfig;
import io.fabric8.openshift.api.model.monitoring.v1alpha1.AlertmanagerConfigList;
import io.fabric8.openshift.client.OpenShiftClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2024/11/18 0:44
 **/
@RestController
@RequestMapping("/alert")
public class AlertManagerController {

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        OpenShiftClient openShiftClient = K8sUtil.createOClient();
        List<AlertmanagerConfig> alertmanagerConfigs;
        try {
            AlertmanagerConfigList alertmanagerConfigList = openShiftClient.monitoring().alertmanagerConfigs().inAnyNamespace().list();
            alertmanagerConfigs = alertmanagerConfigList.getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(alertmanagerConfigs);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> alertmanagerConfigs = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(alertmanagerConfigs);
    }
}
