package vip.gpg123.common.consumer;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.service.EmailService;

/**
 * 消息处理中心
 */
@Component
public class MessageConsumer {

    @Autowired
    private EmailService emailService;

    /**
     * 当消费者从队列取出消息时的回调方法
     *
     * @param email 消息
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "cloud-server-email"))  //表示RabbitMQ消费者,声明一个队列
    public void receive(EmailBody email) {
        // 接收人
        String[] tos = email.getTos();
        String to = ArrayUtil.join(tos, ",");
        Console.log("开始发送邮件:{}", to);
        // 执行发送
        emailService.sendSimpleMail(email.getTitle(), email.getContent(), to);
        Console.log("发送完成");
        // 站内通知
    }
}