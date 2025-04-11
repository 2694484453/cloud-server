
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;

/**
 * 生产者测额是
 */
@SpringBootTest(classes = CloudServerApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AmqpTest {

    private static final String queue = "cloud-server-email";

    //注入rabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送hello world
    @Test
    public void testHelloWorld() {
        //转换和发送    1.routingKey 2.消息
        rabbitTemplate.convertAndSend(queue, "hello world");
    }
}