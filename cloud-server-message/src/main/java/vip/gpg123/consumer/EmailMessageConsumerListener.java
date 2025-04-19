package vip.gpg123.consumer;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.config.EmailClient;
import vip.gpg123.domain.Email;

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
public class EmailMessageConsumerListener {

    @Autowired
    private EmailClient emailClient;

    /**
     * 当消费者从队列取出消息时的回调方法
     * @param email 消息
     */
    @RabbitHandler
    public void receive(Email email) {
        // 接收人
        String[] tos = email.getTos();
        String to =ArrayUtil.join(tos,",");
        Console.log("开始发送邮件:{}",to);
        // 执行发送
        emailClient.send(to, email.getTitle(), email.getContent());
    }
}