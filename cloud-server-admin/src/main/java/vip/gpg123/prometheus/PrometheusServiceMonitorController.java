package vip.gpg123.prometheus;

import io.fabric8.openshift.api.model.monitoring.v1.ServiceMonitor;
import io.fabric8.openshift.client.OpenShiftClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/7 22:55
 **/
@RestController
@RequestMapping("/prometheus/serviceMonitor")
@Api(tags = "【serviceMonitor】管理")
public class PrometheusServiceMonitorController {

    @Qualifier("OpenShiftClient")
    @Autowired
    private OpenShiftClient openShiftClient;

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = getServiceMonitors();
        return PageUtils.toPage(list);
    }

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> list = getServiceMonitors();
        return AjaxResult.success("查询成功", list);
    }

    /**
     * 查询资源
     *
     * @return r
     */
    private List<?> getServiceMonitors() {
        List<ServiceMonitor> list;
        try {
            list = openShiftClient.monitoring().serviceMonitors().inAnyNamespace().list().getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
