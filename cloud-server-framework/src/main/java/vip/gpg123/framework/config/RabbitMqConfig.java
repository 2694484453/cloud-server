package vip.gpg123.framework.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * amqp
 */
@Component
public class RabbitMqConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendRegistrationMessage(String msg) {
        rabbitTemplate.convertAndSend("registration", "", msg);
    }

    public void sendLoginMessage(String msg) {
        rabbitTemplate.convertAndSend("login", "", msg);
    }
}
