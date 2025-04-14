package vip.gpg123.config;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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

    /**
     * 当消费者从队列取出消息时的回调方法
     * @param message 消息
     */
    @RabbitHandler
    public void receive(String message) {
        System.out.println("message = " + message);
    }
}