package vip.gpg123.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.git.domain.GitCodeSpace;

@Service
public class GitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String gitExchange = "cloud-server-git";

    public static final String gitQueue = "cloud-server-git";

    /**
     * 仓库克隆
     *
     * @param gitCodeSpace git
     */
    public void gitClone(GitCodeSpace gitCodeSpace) {
        rabbitTemplate.convertAndSend(gitExchange, "gitClone", gitCodeSpace);
    }
}
