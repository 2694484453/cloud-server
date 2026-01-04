package vip.gpg123.amqp.consumer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.amqp.producer.PrometheusProducer;
import vip.gpg123.framework.config.MonitorConfig;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.service.PrometheusApi;
import vip.gpg123.prometheus.service.PrometheusTargetService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class PrometheusConsumer {

    @Autowired
    private PrometheusTargetService prometheusTargetService;

    @Autowired
    private PrometheusApi prometheusApi;

    @Autowired
    private MonitorConfig.PrometheusProperties prometheusProperties;

    /**
     * 创建json文件
     * @param prometheusTarget pe
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = PrometheusProducer.prometheusQueue, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = PrometheusProducer.prometheusExchange), // 声明直接交换器
            key = "createExporterFile" // 定义路由键
    ))
    public void createExporterFile(PrometheusTarget prometheusTarget) {
        String typePath = prometheusProperties.getPath() + "/" + prometheusTarget.getExporterType();
        if (!FileUtil.exist(typePath)) {
            FileUtil.mkdir(typePath);
        }
        // 位置
        String filePath = typePath + "/" + prometheusTarget.getJobName()+".json";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("labels", new HashMap<String, String>() {{
            put("job", prometheusTarget.getJobName());
        }});
        jsonObject.put("targets", new ArrayList<String>() {{
            add(prometheusTarget.getTargets());
        }});
        jsonArray.add(jsonObject);
        String jsonFormat = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonArray));
        // 写入
        FileUtil.writeString(jsonFormat, filePath, StandardCharsets.UTF_8);
    }

    /**
     * 更新状态
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = PrometheusProducer.prometheusQueue, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = PrometheusProducer.prometheusExchange), // 声明直接交换器
            key = "syncStatus" // 定义路由键
    ))
    public void syncStatus() {

    }
}
