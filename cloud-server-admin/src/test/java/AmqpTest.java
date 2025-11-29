import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.common.core.domain.model.EmailBody;

/**
 * 生产者测额是
 */
@SpringBootTest(classes = CloudServerApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AmqpTest {

    private static final String queue = "testQueue";

    //注入rabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送hello world
    @Test
    public void test() {
        //转换和发送    1.routingKey 2.消息
        rabbitTemplate.convertAndSend(queue, "hello world");
    }

    @Test
    public void testSendEmail() {
        //
        EmailBody  emailBody = new EmailBody();
        emailBody.setHtml(false);
        emailBody.setTitle("mq发送");
        emailBody.setContent("hello world");
        emailBody.setTos(new String[]{"2694484453@qq.com"});
        rabbitTemplate.convertAndSend("cloud-server-message",emailBody);
    }
}