package vip.gpg123.system.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

    @RabbitListener(queues = "testQueue")
    public void test(String message) {
        System.out.println("接收到了消息: " + message);
    }
}
