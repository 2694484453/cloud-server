package vip.gpg123.prometheus.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.prometheus.dto.PrometheusQueryResponse;
import vip.gpg123.prometheus.dto.PrometheusTargetResponse;

@Service
@FeignClient(name = "prometheus-api", url = "${monitor.prometheus.url}")
public interface PrometheusApi {

    /**
     * 查询targets
     *
     * @param state s
     * @return r
     */
    @GetMapping("/api/v1/targets")
    PrometheusTargetResponse targets(@RequestParam(value = "state", required = false, defaultValue = "active") String state);

    /**
     * 动态加载alert配置
     */
    @PostMapping("/-/reload")
    void reload();

    /**
     * 规则
     * @param type t
     * @return r
     */
    @GetMapping("/api/v1/rules")
    JSONObject rules(@RequestParam(value = "type", required = false, defaultValue = "alert") String type);

    /**
     * 检查
     * @param query q
     * @param stats s
     * @return r
     */
    @GetMapping("/api/v1/query")
    PrometheusQueryResponse query(@RequestParam(value = "query") String query,
                                  @RequestParam(value = "stats") Boolean stats);

}
