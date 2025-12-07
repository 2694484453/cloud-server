package vip.gpg123.framework.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * amqp
 */
@Configuration
public class RabbitMQConfig {

    public static final String routingKey = "cloud-server";

    public static final String exchange = "cloud-server-exchange";

    public static final String emailQueue = "cloud-server-email";

    public static final String noticeQueue = "cloud-server-notice";

    public static final String devopsQueue = "cloud-server-devops";


    public static final String gitQueue = "cloud-server-git";

    /**
     * cloud-server-exchange
     * @return r
     */
    @Bean(name = exchange)
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * 邮件
     *
     * @return r
     */
    @Bean(name = emailQueue)
    public Queue emailQueue() {
        return new Queue(emailQueue, true); // true表示持久化队列
    }

    @Bean(name = "bindingEmail")
    public Binding bindingEmail(@Qualifier(value = emailQueue) Queue queue, @Qualifier(RabbitMQConfig.exchange) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * 消息
     *
     * @return r
     */
    @Bean(name = noticeQueue)
    public Queue noticeQueue() {
        return new Queue(noticeQueue, true); // true表示持久化队列
    }

    @Bean(name = "bindingNotice")
    public Binding bindingNotice(@Qualifier(value = noticeQueue) Queue queue, @Qualifier(RabbitMQConfig.exchange) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * devops
     *
     * @return r
     */
    @Bean(name = devopsQueue)
    public Queue devopsQueue() {
        return new Queue(devopsQueue, true);
    }

    @Bean(name = "bindingDevops")
    public Binding bindingDevops(@Qualifier(value = devopsQueue) Queue queue, @Qualifier(RabbitMQConfig.exchange) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }


}
