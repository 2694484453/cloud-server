package vip.gpg123.framework.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * amqp
 */
@Configuration
public class RabbitMQConfig {

    @Bean(name = "testQueue")
    public Queue testQueue() {
        return new Queue("testQueue", true);
    }

    // 定义队列
    @Bean(name = "emailQueue")
    public Queue emailQueue() {
        return new Queue("cloud-server-message-email", true); // true表示持久化队列
    }

    @Bean(name = "noticeQueue")
    public Queue noticeQueue() {
        return new Queue("cloud-server-message-notice", true); // true表示持久化队列
    }

    @Bean(name = "testExchange")
    public DirectExchange testExchange() {
        return new DirectExchange("testExchange");
    }

    // 定义直连交换机
    @Bean(name = "cloudServerExchange")
    public DirectExchange messageExchange() {
        return new DirectExchange("cloud-server-exchange");
    }

    // 绑定队列到交换机
    @Bean(name = "bindingEmail")
    public Binding bindingEmail(@Qualifier(value = "emailQueue") Queue queue, @Qualifier(value = "cloudServerExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("cloud-server-message");
    }

    @Bean(name = "bindingNotice")
    public Binding bindingNotice(@Qualifier(value = "noticeQueue") Queue queue, @Qualifier(value = "cloudServerExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("cloud-server-message");
    }


}
