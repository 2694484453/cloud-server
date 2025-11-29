package vip.gpg123.system.consumer;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.service.EmailService;

/**
 * 邮件消息处理
 */
@Component
@Slf4j
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    /**
     * 当消费者从队列取出消息时的回调方法
     *
     * @param email 消息
     */
    @RabbitListener(queues = "cloud-server-message-email")
    public void receive(EmailBody email) {
        // 接收人
        String[] tos = email.getTos();
        String to = ArrayUtil.join(tos, ",");
        // 执行发送
        emailService.sendSimpleMail(email.getTitle(), email.getContent(), to);
        log.info("{}:邮件发送完成", to);
    }
}