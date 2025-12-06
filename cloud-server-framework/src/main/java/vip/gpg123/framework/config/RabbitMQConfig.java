package vip.gpg123.framework.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * amqp
 */
@Configuration
public class RabbitMQConfig {

    private static final String routingKey = "cloud-server";

    private static final String exchange = "cloud-server-exchange";

    private static final String testExchange = "test-exchange";

    private static final String testQueue = "test-queue";

    private static final String emailQueue = "cloud-server-email";

    private static final String noticeQueue = "cloud-server-notice";

    private static final String devopsQueue = "cloud-server-devops";

    /**
     * 测试
     *
     * @return r
     */
    @Bean(name = testQueue)
    public Queue testQueue() {
        return new Queue(testQueue, true);
    }

    @Bean(name = testExchange)
    public DirectExchange testExchange() {
        return new DirectExchange(testExchange);
    }

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
