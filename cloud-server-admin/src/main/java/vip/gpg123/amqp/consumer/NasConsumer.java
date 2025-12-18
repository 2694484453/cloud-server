package vip.gpg123.amqp.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.amqp.producer.NasProducer;
import vip.gpg123.nas.domain.FrpServerHttp;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.FrpServerApi;
import vip.gpg123.nas.service.NasFrpClientService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NasConsumer {

    @Autowired
    private FrpServerApi frpServerApi;

    @Autowired
    private NasFrpClientService nasFrpClientService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = NasProducer.nasQueue, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = NasProducer.nasExchange), // 声明直接交换器
            key = "syncStatus" // 定义路由键
    ))
    public void syncStatus() {

    }
}
