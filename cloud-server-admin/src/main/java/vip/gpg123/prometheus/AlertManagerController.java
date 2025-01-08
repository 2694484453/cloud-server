package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;
import io.fabric8.openshift.api.model.monitoring.v1alpha1.AlertmanagerConfig;
import io.fabric8.openshift.api.model.monitoring.v1alpha1.AlertmanagerConfigList;
import io.fabric8.openshift.client.OpenShiftClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OpenShiftClient openShiftClient;

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<AlertmanagerConfig> alertManagerConfigs;
        try {
            AlertmanagerConfigList alertmanagerConfigList = openShiftClient.monitoring().alertmanagerConfigs().inAnyNamespace().list();
            alertManagerConfigs = alertmanagerConfigList.getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(alertManagerConfigs);
    }

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        AjaxResult ajaxResult = list();
        List<?> alertManagerConfigs = Convert.toList(ajaxResult.get("data"));
        return PageUtils.toPage(alertManagerConfigs);
    }
}
