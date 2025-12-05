package vip.gpg123.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vip.gpg123.common.utils.bean.BeanUtils;
import vip.gpg123.devops.domain.DevopsJob;
import vip.gpg123.devops.domain.Git;

@Component
public class DevopsConsumer {

    /**
     * 创建job
     *
     * @param devopsJob job
     */
    @RabbitListener(queues = "cloud-server-devops")
    public void receiveDevopsJob(DevopsJob devopsJob) {
        Git git = new Git();
        BeanUtils.copyProperties(devopsJob, git);

    }
}
