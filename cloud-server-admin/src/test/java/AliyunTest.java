
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.domain.service.AliYunDomainApi;

/**
 * 生产者测额是
 */
@SpringBootTest(classes = CloudServerApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private AliYunDomainApi aliYunDomainApi;

    //add
    @Test
    public void add() {
        //转换和发送    1.routingKey 2.消息
       aliYunDomainApi.addDomainRecord("gpg123.vip", "A", "local-test", "192.168.1.1");
    }
}