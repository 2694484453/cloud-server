package vip.gpg123.prometheus;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/8 0:00
 **/
@RestController
@RequestMapping("/prometheus/rules")
@Api(tags = "【rules】查询")
public class PrometheusRulesController {

    @Value("${monitor.prometheus.endpoint}")
    private String endpoint;

    /**
     * 分页查询
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = rules();
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> list = rules();
        return AjaxResult.success(list);
    }

    /**
     * 获取rules
     * @return r
     */
    private List<?> rules(){
        HttpResponse httpResponse = HttpUtil.createGet(endpoint+"/api/v1/rules")
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
        JSONArray groups = JSONUtil.parseArray(data.get("groups"));
        return Convert.toList(groups);
    }
}
