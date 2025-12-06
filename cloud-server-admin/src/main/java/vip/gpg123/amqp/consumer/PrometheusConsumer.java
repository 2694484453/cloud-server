package vip.gpg123.amqp.consumer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.gpg123.framework.config.RabbitMQConfig;
import vip.gpg123.platform.domain.ActiveTarget;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusExporterService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PrometheusConsumer {

    @Autowired
    private PrometheusExporterService prometheusExporterService;

    @Autowired
    private PrometheusApi prometheusApi;

    @Value("${monitor.prometheus.exporterPath}")
    private String exporterPath;


    @RabbitListener(queues = RabbitMQConfig.prometheusQueue)
    public void createExporterFile(PrometheusExporter prometheusExporter) {
        // 位置
        String filePath = exporterPath + "/" + prometheusExporter.getExporterType() + "/" + prometheusExporter.getJobName()+".json";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("labels", new HashMap<String, String>() {{
            put("job", prometheusExporter.getJobName());
        }});
        jsonObject.put("targets", new ArrayList<String>() {{
            add(prometheusExporter.getTargets());
        }});
        jsonArray.add(jsonObject);
        String jsonFormat = JSONUtil.toJsonStr(jsonArray);
        // 写入
        FileUtil.writeString(jsonFormat, filePath, StandardCharsets.UTF_8);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConfig.prometheusQueue, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = RabbitMQConfig.exchange), // 声明直接交换器
            key = RabbitMQConfig.prometheusQueue+"-syncStatus" // 定义路由键
    ))
    public void syncStatus() {
        // 查询数据库中
        List<PrometheusExporter> prometheusExporterList = prometheusExporterService.list();
        Map<String, PrometheusExporter> map = prometheusExporterList.stream().collect(Collectors.toMap(PrometheusExporter::getJobName, item -> item));
        // 查询状态prometheus
        PrometheusTargetResponse response = prometheusApi.targets("");
        List<ActiveTarget> list = Convert.toList(ActiveTarget.class, JSONUtil.parseArray(response.getData().getActiveTargets()));
        // 循环
        list.forEach(target -> {
            JSONObject object = JSONUtil.parseObj(target.getDiscoveredLabels());
            String jobName = object.getStr("job");
            String status = target.getHealth();
            if (map.containsKey(jobName)) {
                PrometheusExporter exporter = map.get(jobName);
                exporter.setStatus(status);
                exporter.setGlobalUrl(target.getGlobalUrl());
                prometheusExporterService.updateById(exporter);
            }
        });
    }
}
