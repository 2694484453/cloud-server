package vip.gpg123.tracing.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jaeger-api", url = "https://jaeger.gpg123.vip")
public interface JaegerApi {

    /**
     * 获取服务列表
     *
     * @return r
     */
    @GetMapping("/api/services")
    JSONObject getServices();


    /**
     * traces
     *
     * @return r
     */
    @GetMapping("/api/traces")
    JSONObject getTraces(@RequestParam("service") String service,
                         @RequestParam("limit") String limit,
                         @RequestParam("start") String start,
                         @RequestParam("end") String end,
                         @RequestParam("lookBack") String lookBack,
                         @RequestParam("operation") String operation);
}
