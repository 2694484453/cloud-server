package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.config.RabbitMQConfig;
import vip.gpg123.prometheus.domain.PrometheusExporter;

@Service
public class PrometheusProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建exporter文件
     * @param prometheusExporter e
     */
    public void createExporterFile(PrometheusExporter prometheusExporter) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.prometheusQueue, prometheusExporter);
    }

    /**
     * 同步状态
     */
    public void syncStatus() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.prometheusQueue+"-syncStatus","");
    }



}
