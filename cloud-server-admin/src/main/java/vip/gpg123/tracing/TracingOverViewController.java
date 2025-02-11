package vip.gpg123.tracing;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;

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
@RequestMapping("/tracing/traces")
@Api(tags = "【tracing】链路追踪-概览")
public class TracingOverViewController {

    @Autowired
    private TracingAppController tracingAppController;

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
        map.put("apps", list.size());
        return AjaxResult.success(map);
    }
}
