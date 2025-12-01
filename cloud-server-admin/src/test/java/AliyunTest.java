
import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.AddDomainRecordResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.common.feign.AliYunDomainApi;

/**
 * 生产者测额是
 */
@SpringBootTest(classes = CloudServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private AliYunDomainApi aliYunDomainApi;

    @Test
    public void add2() {

        AddDomainRecordRequest request = new AddDomainRecordRequest()
                .setDomainName("1")
                .setRR("1")
                .setType("1")
                .setValue("1");
        AddDomainRecordResponseBody body = aliYunDomainApi.addDomainRecord(request);
    }
}