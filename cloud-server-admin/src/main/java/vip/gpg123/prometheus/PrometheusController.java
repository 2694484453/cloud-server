package vip.gpg123.prometheus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.dto.PrometheusQueryResponse;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prometheus")
public class PrometheusController extends BaseController {

    @Autowired
    private PrometheusTargetService prometheusTargetService;

    @Autowired
    private PrometheusApi prometheusApi;

    /**
     * 概览
     *
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("title", "当前系统接入端点总数");
        totalMap.put("count", prometheusTargetService.count());
        list.add(totalMap);

        Map<String, Object> map = new HashMap<>();
        map.put("title", "我的接入端点总数");
        map.put("count", prometheusTargetService.count(new LambdaQueryWrapper<PrometheusTarget>()
                .eq(PrometheusTarget::getCreateBy, getUserId())
        ));
        list.add(map);
        //
        Map<String, Object> health = new HashMap<>();
        health.put("title", "我的接入端点健康数量");
        health.put("count", prometheusTargetService.count(new LambdaQueryWrapper<PrometheusTarget>()
                .eq(PrometheusTarget::getCreateBy, getUserId())
                .eq(PrometheusTarget::getStatus, "up")
        ));
        list.add(health);
        //
        Map<String, Object> down = new HashMap<>();
        down.put("title", "我的接入端点异常数量");
        down.put("count", prometheusTargetService.count(new LambdaQueryWrapper<PrometheusTarget>()
                .eq(PrometheusTarget::getCreateBy, getUserId())
                .eq(PrometheusTarget::getStatus, "down")
        ));
        list.add(down);
        return AjaxResult.success(list);
    }

    /**
     * 查询
     * @param query q
     * @return r
     */
    @GetMapping("/query")
    public AjaxResult query(@RequestParam("query") String query) {
        PrometheusQueryResponse response = prometheusApi.query(query,true);
        return "success".equals(response.getStatus()) ? AjaxResult.success(response.getStatus()) : AjaxResult.error(response.getError());
    }
}
