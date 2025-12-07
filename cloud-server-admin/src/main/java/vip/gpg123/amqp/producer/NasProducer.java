package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NasProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String nasExchange = "cloud-server-nas";

    public static final String nasQueue = "cloud-server-nas";

    /**
     * 同步状态
     */
    public void syncStatus(){
        rabbitTemplate.convertAndSend(nasExchange,"syncStatus","");
    }
}
