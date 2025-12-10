package vip.gpg123.amqp.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestConsumer {

    public static final String testExchange = "test-exchange";

    public static final String testQueue = "test-queue";
//
//    @RabbitListener(queues = testQueue)
//    public void test1(String message) {
//        System.out.println("接收到了消息1: " + message);
//    }
//
//    @RabbitListener(queues = testQueue)
//    public void test2(String message) {
//        System.out.println("接收到了消息2: " + message);
//    }
//
//    //高级用法：动态声明绑定关系
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = testQueue + "-t3", durable = "true"), // 创建持久化队列
//            exchange = @Exchange(name = testExchange + "-t3"), // 声明直接交换器
//            key = "test3" // 定义路由键
//    ))
//    public void test3(String message) {
//        System.out.println("接收到了消息3: " + message);
//    }
//
//    //@RabbitHandler允许你在同一个类中定义不同的方法来处理不同类型的消息
//    @RabbitHandler
//    public void test4(String message) {
//        System.out.println("<UNK>4: " + message);
//    }
//
//    // 可以接收原始Message对象，获取完整信息（消息体、消息头等）
//    @RabbitHandler
//    public void handleGenericMessage(Message message) {
//        // 处理原始消息
//    }
}
