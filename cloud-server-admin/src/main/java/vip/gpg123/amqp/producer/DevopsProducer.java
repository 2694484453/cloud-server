package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.devops.domain.DevopsJob;

@Service
public class DevopsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String emailRoutingKey = "cloud-server-devops";

    private static final String actionNoticeRoutingKey = "cloud-server-devops-action";

    /**
     * 创建job
     * @param devopsJob dev
     */
    public void createDevopsJob(DevopsJob devopsJob){
        rabbitTemplate.convertAndSend(emailRoutingKey, devopsJob);
    }
}
