package vip.gpg123.tracing;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/2/7 1:42
 **/
@RestController
@RequestMapping("/tracing")
@Api(tags = "【tracing】链路追踪")
public class TracingController {

    @Value("${trace.api}")
    private String api;

    /**
     * 分页
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page() {
        List<?> list = getServiceList();
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list() {
        List<?> list = getServiceList();
        return AjaxResult.success(list);
    }

    /**
     * 查询服务
     *
     * @return r
     */
    private List<?> getServiceList() {
        HttpResponse httpResponse = HttpUtil.createGet(api + "/services")
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));
        List<?> list = Convert.toList(jsonArray);
        List<Map<String, Object>> result = new ArrayList<>();
        list.forEach(e -> {
            result.add(new HashMap<String, Object>() {{
                put("name", e.toString());
                put("status", "ok");
                put("description", "");
                put("type", "jaeger");
            }});
        });
        return result;
    }
}
