package vip.gpg123.tracing;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/11 0:58
 **/
@RestController
@RequestMapping("/tracing/traces")
@Api(tags = "【tracing】链路追踪-调用链")
public class TracingTracesController {

    @Value("${trace.api}")
    private String api;

    /**
     * 分页
     *
     * @return r
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public TableDataInfo page(@RequestParam(value = "service", required = false) String service,
                              @RequestParam(value = "limit", required = false) String limit,
                              @RequestParam(value = "start", required = false) String start,
                              @RequestParam(value = "end", required = false) String end,
                              @RequestParam(value = "lookBack", required = false) String lookBack,
                              @RequestParam(value = "operation", required = false) String operation) {
        List<?> list = getServiceList(service, limit, start, end, lookBack, operation);
        return PageUtils.toPage(list);
    }

    /**
     * 列表查询
     *
     * @return r
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表查询")
    public AjaxResult list(@RequestParam(value = "service", required = false) String service,
                           @RequestParam(value = "limit", required = false) String limit,
                           @RequestParam(value = "start", required = false) String start,
                           @RequestParam(value = "end", required = false) String end,
                           @RequestParam(value = "lookBack", required = false) String lookBack,
                           @RequestParam(value = "operation", required = false) String operation) {
        List<?> list = getServiceList(service, limit, start, end, lookBack, operation);
        return AjaxResult.success(list);
    }


    /**
     * 查询链路
     *
     * @return r
     */
    private List<?> getServiceList(String service, String limit, String start, String end, String lookBack, String operation) {
        HttpResponse httpResponse = HttpUtil.createGet(api + "/api/traces?end=" + StrUtil.blankToDefault(end, "") + "&limit=" + StrUtil.blankToDefault(limit, "") + "&lookback=" + StrUtil.blankToDefault(lookBack, "") + "&maxDuration=&minDuration=&service=" + StrUtil.blankToDefault(service, "") + "&start=" + StrUtil.blankToDefault(start, "") + "&operation=" + StrUtil.blankToDefault(operation, ""))
                .timeout(10000)
                .setConnectionTimeout(10000)
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONArray data = JSONUtil.parseArray(jsonObject.get("data"));
        return Convert.toList(data);
    }
}
