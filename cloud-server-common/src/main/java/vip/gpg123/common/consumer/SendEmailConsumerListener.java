package vip.gpg123.common.consumer;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.service.EmailService;

/**
 * 邮件消费者
 * 生产端没有指定交换机只有routingKey和Object。
 * 消费方产生hello队列，放在默认的交换机(AMQP default)上。
 * 而默认的交换机有一个特点，只要你的routerKey的名字与这个
 * 交换机的队列有相同的名字，他就会自动路由上。
 * 生产端routingKey 叫hello ，消费端生产hello队列。
 * 他们就路由上了
 */
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "cloud-server-email"))  //表示RabbitMQ消费者,声明一个队列
@Slf4j
public class SendEmailConsumerListener {

    @Autowired
    private EmailService emailService;

    /**
     * 当消费者从队列取出消息时的回调方法
     *
     * @param emailBody 消息
     */
    @RabbitHandler
    public void receive(EmailBody emailBody) {
        // 接收人
        String[] tos = emailBody.getTos();
        String to = ArrayUtil.join(tos, ",");
        log.info("开始发送邮件给:{}", to);
        // 执行发送
        emailService.sendSimpleMail("", "", emailBody.getTos());
        log.info("发送完成");
        // 站内通知

    }
}