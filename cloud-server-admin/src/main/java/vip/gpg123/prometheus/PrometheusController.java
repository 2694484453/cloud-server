package vip.gpg123.prometheus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.service.PrometheusExporterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prometheus")
public class PrometheusController extends BaseController {

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    /**
     * 概览
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "我的接入端点总数");
        map.put("count", prometheusExporterService.count(new LambdaQueryWrapper<PrometheusExporter>()
                .eq(PrometheusExporter::getCreateBy, getUserId())
        ));
        list.add(map);
        //
        Map<String, Object> health = new HashMap<>();
        health.put("title", "健康数量");
        health.put("count", prometheusExporterService.count(new LambdaQueryWrapper<PrometheusExporter>()
                .eq(PrometheusExporter::getCreateBy, getUserId())
                .eq(PrometheusExporter::getStatus, "up")
        ));
        list.add(health);
        //
        Map<String, Object> down = new HashMap<>();
        down.put("title", "异常数量");
        down.put("count", prometheusExporterService.count(new LambdaQueryWrapper<PrometheusExporter>()
                .eq(PrometheusExporter::getCreateBy, getUserId())
                .eq(PrometheusExporter::getStatus, "down")
        ));
        list.add(down);
        return AjaxResult.success(list);
    }
}
