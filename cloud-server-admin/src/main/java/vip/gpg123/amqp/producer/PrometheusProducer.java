package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.prometheus.domain.PrometheusExporter;

@Service
public class PrometheusProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String prometheusQueue = "cloud-server-prometheus";

    public static final String prometheusExchange = "cloud-server-prometheus";

    /**
     * 创建exporter文件
     *
     * @param prometheusExporter e
     */
    public void createExporterFile(PrometheusExporter prometheusExporter) {
        rabbitTemplate.convertAndSend(prometheusExchange, "createExporterFile", prometheusExporter);
    }

    /**
     * 同步状态
     */
    public void syncStatus() {
        rabbitTemplate.convertAndSend(prometheusExchange, "syncStatus", "");
    }


}
