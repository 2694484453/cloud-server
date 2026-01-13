package vip.gpg123.tracing;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.framework.config.MonitorConfig;
import vip.gpg123.tracing.service.JaegerApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: 【tracing】链路追踪-概览
 * @date 2025/2/11 16:52
 */
@RestController
@RequestMapping("/tracing")
@Api(tags = "【tracing】链路追踪-概览")
public class TracingOverViewController {

    @Autowired
    private TracingAppController tracingAppController;

    @Autowired
    private MonitorConfig.PrometheusProperties prometheusProperties;

    @Autowired
    private JaegerApi jaegerApi;

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    @ApiOperation(value = "概览")
    public AjaxResult overView() {
        List<?> list = tracingAppController.getServiceList();
        Map<String, Object> map = new HashMap<>();
        // 应用总数
        map.put("apps", getAppTotal());
        map.put("upTime", queryUpTime());
        return AjaxResult.success(map);
    }

    public String getAppTotal() {
        JSONObject jsonObject = jaegerApi.getServices();
        return jsonObject.get("total").toString();
    }

    public Object queryUpTime() {
        HttpResponse httpResponse = HttpUtil.createGet(prometheusProperties.getUrl() + "/api/v1/query?query=otelcol_process_uptime{service='opentelemetry'}")
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
        JSONArray results = JSONUtil.parseArray(data.get("result"));
        JSONObject result = JSONUtil.parseObj(results.get(0));
        return null;
    }
}
