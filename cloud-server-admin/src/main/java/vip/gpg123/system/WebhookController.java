package vip.gpg123.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.prometheus.domain.AlertDTO;
import vip.gpg123.prometheus.domain.AlertManagerDTO;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import vip.gpg123.prometheus.service.PrometheusAlertService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private PrometheusAlertService prometheusAlertService;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    /**
     * 接收 Prometheus Alertmanager 的告警
     */
    @PostMapping(value = "/alert", consumes = "application/json")
    public AjaxResult receiveAlert(@RequestBody AlertManagerDTO alertDTO) {

        // 1. 获取告警状态 (firing 或 resolved)
        String status = alertDTO.getStatus();
        logger.info("收到告警通知，状态: {}", status);

        // 2. 遍历告警列表
        List<AlertDTO> alerts = alertDTO.getAlerts();
        for (AlertDTO alert : alerts) {
            // 获取标签 (Labels)
            String alertName = alert.getLabels().get("alertname");
            String instance = alert.getLabels().get("instance"); // 出问题的实例
            String severity = alert.getLabels().get("severity"); // 级别: warning/critical
            String createBy = alert.getLabels().get("createBy");

            // 获取注解 (Annotations)
            String summary = alert.getAnnotations().get("summary");
            String description = alert.getAnnotations().get("description");

            // 3. 在这里编写你的处理逻辑
            // 例如：发送钉钉、记录日志、存入数据库、调用运维脚本等
            handleAlert(alertName, createBy, instance, severity, summary, description, status);
        }

        // 4. 必须返回 200 OK
        // Alertmanager 收到 200 才会认为消息已送达
        // 如果返回非 200，它会根据配置进行重试
        return AjaxResult.success();
    }

    /**
     * 处理告警的业务逻辑
     */
    private void handleAlert(String alertName, String createBy, String instance, String severity,
                             String summary, String description, String status) {

        // TODO: 在这里集成你的业务逻辑
        // 例如：调用钉钉机器人发送消息
        // 例如：调用短信网关
        // 例如：记录到数据库

        System.out.println("===================================");
        System.out.println("告警名称: " + alertName);
        System.out.println("创建人: " + createBy);
        System.out.println("实例: " + instance);
        System.out.println("级别: " + severity);
        System.out.println("状态: " + status);
        System.out.println("摘要: " + summary);
        System.out.println("详情: " + description);
        System.out.println("===================================");
        // 保存数据库
        PrometheusAlert alert = new PrometheusAlert();
        alert.setAlertName(alertName);
        alert.setCreateBy(createBy);
        alert.setCreateTime(new Date());
        alert.setAlertLevel(severity);
        alert.setDescription(description);
        prometheusAlertService.save(alert);
    }
}
