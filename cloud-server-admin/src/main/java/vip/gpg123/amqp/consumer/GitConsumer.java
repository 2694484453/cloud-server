package vip.gpg123.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vip.gpg123.framework.config.RabbitMQConfig;
import vip.gpg123.git.domain.GitRepo;

@Component
public class GitConsumer {

    @RabbitListener(queues = RabbitMQConfig.prometheusQueue)
    public void gitClone(GitRepo gitRepo) {

    }
}
