package vip.gpg123.caddy.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * caddy-api
 */
@Service
@FeignClient(name = "caddy-api", url = "${caddy.api}")
public interface CaddyApi {

    /**
     * 指标查询
     *
     * @return r
     */
    @GetMapping("/metrics")
    String metrics();

    /**
     * 配置查询
     *
     * @return r
     */
    @GetMapping("/config/")
    JSONObject config();

    /**
     * 添加
     *
     * @param state s
     * @return r
     */
    @PostMapping("/config/")
    JSONObject config(@RequestParam(value = "state", required = false, defaultValue = "active") String state);

    /**
     * 热重载配置
     *
     * @return r
     */
    @PostMapping("/load")
    JSONObject load();

    /**
     * 停止
     *
     * @return r
     */
    @PostMapping("/stop")
    JSONObject stop();

}
