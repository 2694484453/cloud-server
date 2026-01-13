package vip.gpg123.tracing;

import cn.hutool.core.convert.Convert;
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
import vip.gpg123.tracing.service.JaegerApi;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/11 0:58
 **/
@RestController
@RequestMapping("/tracing/traces")
@Api(tags = "【tracing】链路追踪-调用链")
public class TracingTracesController {

    @Autowired
    private JaegerApi jaegerApi;

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
        JSONObject jsonObject = jaegerApi.getTraces(service,limit,start,end,lookBack,operation);
        JSONArray data = JSONUtil.parseArray(jsonObject.get("data"));
        return Convert.toList(data);
    }
}
