package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import vip.gpg123.platform.domain.ActiveTarget;
import vip.gpg123.platform.domain.PrometheusTargetData;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.common.feign.PrometheusApi;

import java.util.ArrayList;
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
    private PrometheusApi prometheusApi;

    /**
     * 分页查询
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "job", required = false) String job,
                              @RequestParam(value = "health", required = false) String health) {
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> searchList = new ArrayList<>();
        List<ActiveTarget> list = targets(response.getData());
        list.forEach(target -> {
            JSONObject object = JSONUtil.parseObj(target.getDiscoveredLabels());
            String jobName = object.getStr("job");
            String healthName = target.getHealth();
            if (StrUtil.isNotBlank(job)) {
                if (jobName.contains(job)) {
                    searchList.add(target);
                }
            }
            if (StrUtil.isNotBlank(health)) {
                if (healthName.contains(health)) {
                    searchList.add(target);
                }
            }
        });
        return PageUtils.toPage(StrUtil.isNotBlank(job) || StrUtil.isNotBlank(health) ? searchList:list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "state", required = false) String state) {
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> list = targets(response.getData());
        return AjaxResult.success(list);
    }

    /**
     * 获取目标
     *
     * @return r
     */
    private List<ActiveTarget> targets(PrometheusTargetData data) {
        JSONArray activeTargets = JSONUtil.parseArray(data.getActiveTargets());
        return Convert.toList(ActiveTarget.class, activeTargets);
    }
}
