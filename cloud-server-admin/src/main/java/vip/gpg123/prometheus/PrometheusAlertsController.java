package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.framework.client.PrometheusClient;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/7 22:58
 **/
@RestController
@RequestMapping("/prometheus/alerts")
@Api(tags = "【alerts】管理")
public class PrometheusAlertsController {

    @Autowired
    private PrometheusClient prometheusClient;

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = alertList();
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     * @param name 名称
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "name", required = false) String name) {
        List<?> list = alertList();
        return AjaxResult.success(list);
    }

    /**
     * 查询告警列表
     *
     * @return r
     */
    private List<?> alertList() {
        HttpResponse httpResponse = HttpUtil.createGet(prometheusClient.getEndpoint() + "/api/v1/alerts")
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
        JSONArray alerts = JSONUtil.parseArray(data.get("alerts"));
        return Convert.toList(alerts);
    }
}
