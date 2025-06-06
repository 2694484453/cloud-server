package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.platform.domain.PlatformServiceInstance;
import vip.gpg123.platform.service.PlatformServiceInstanceService;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.service.PrometheusApi;

import java.net.URI;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/8 0:33
 **/
@RestController
@RequestMapping("/prometheus/targets")
@Api(tags = "【targets】查询")
public class PrometheusTargetsController extends BaseController {

    @Autowired
    private PlatformServiceInstanceService platformServiceInstanceService;

    @Autowired
    private PrometheusApi prometheusApi;

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "name") String name,
                              @RequestParam(value = "state", required = false) String state) {
        // 查询对应的实例设置url
        PlatformServiceInstance platformServiceInstance = platformServiceInstanceService.getOne(new LambdaQueryWrapper<PlatformServiceInstance>()
                .eq(PlatformServiceInstance::getName, name)
                .eq(PlatformServiceInstance::getCreateBy, getUsername())
                .eq(PlatformServiceInstance::getType, "prometheus")
        );
        // 动态设置Prometheus实例地址
        URI dynamicUri = URI.create(platformServiceInstance.getHost());
        PrometheusTargetResponse response = prometheusApi.targets(dynamicUri,state);
        List<?> list = targets(response.getData());
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name") String name,
                           @RequestParam(value = "state", required = false) String state) {
        // 查询对应的实例设置url
        PlatformServiceInstance platformServiceInstance = platformServiceInstanceService.getOne(new LambdaQueryWrapper<PlatformServiceInstance>()
                .eq(PlatformServiceInstance::getName, name)
                .eq(PlatformServiceInstance::getCreateBy, getUsername())
                .eq(PlatformServiceInstance::getType, "prometheus")
        );
        // 动态设置Prometheus实例地址
        URI dynamicUri = URI.create(platformServiceInstance.getHost());
        PrometheusTargetResponse response = prometheusApi.targets(dynamicUri,state);
        List<?> list = targets(response.getData());
        return AjaxResult.success(list);
    }

    /**
     * 获取目标
     *
     * @return r
     */
    private List<?> targets(JSONObject jsonObject) {
        JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
        JSONArray activeTargets = JSONUtil.parseArray(data.get("activeTargets"));
        return Convert.toList(activeTargets);
    }
}
