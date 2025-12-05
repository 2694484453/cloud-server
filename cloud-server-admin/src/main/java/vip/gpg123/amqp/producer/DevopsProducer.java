package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.devops.domain.DevopsJob;

@Service
public class DevopsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String routingKey = "cloud-server-devops";

    /**
     * 创建job
     *
     * @param devopsJob dev
     */
    public void createDevopsJob(DevopsJob devopsJob) {
        rabbitTemplate.convertAndSend(routingKey, devopsJob);
    }
}
