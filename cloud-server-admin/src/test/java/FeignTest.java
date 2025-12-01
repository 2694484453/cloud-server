
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;
import vip.gpg123.common.feign.PrometheusApi;

import java.net.URI;

/**
 * 生产者测额是
 */
@SpringBootTest(classes = CloudServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FeignTest {

    @Autowired
    private PrometheusApi prometheusApi;

    @Value("${monitor.prometheus.endpoint}")
    private String endpoint;

    @Test
    public void add2() {
        // 动态设置Prometheus实例地址
        URI dynamicUri = URI.create("https://prometheus.gpg123.vip");
        PrometheusTargetResponse response = prometheusApi.targets("");
        System.out.println(response);
    }
}