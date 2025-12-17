import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.dashboard.service.UmamiApi;

@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class UmamiTest {

    private static final String token = "TL89uSFeV06lMpJM6aHCftEGk6AvyrA6VTC9xQsN0Nxf6ryxQ/qQdas7M7J0SZRa5uu7fDrsQyxZPjvh1E2KRC6OwrHOZg43wg+mRE5m1sHGysbDavOY3TBjD35gXgZZU81C89hcrgtkuDVAH71i7nzp0B/FgqNN/1uwaSPyb3Fs1wjIUarSY8Wx1F1Q04mOTNJ4EFSSjr41tzXfC1CQuo4huJZsx16zXbcn8u4oFmVGwVdp08sNik/ZSfhEEQaZcDo19/eWn4ROQZjcJkvjVdDGqSA3T/EsoKycxkEhqh64czQxuEgUbJwlMJxX02a4X69xM1aGmwtT1yDPzLR3iqicklP/WUtJ/6DOpA==";

    @Value("${analytics.umami.token}")
    private String umamiToken;

    @Value("${analytics.umami.website-id}")
    private String websiteId;

    @Autowired
    private UmamiApi umamiApi;

    @Test
    public void testUmami() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.GET, "https://cloud.umami.is/analytics/us/api/websites/bdf579c4-ccad-4f66-84ad-12125e206bc9/stats?startAt=1&endAt=1765954799999&unit=hour&timezone=Asia%2FShanghai");
        httpRequest.bearerAuth(umamiToken);
        HttpResponse httpResponse = httpRequest.execute();
        System.out.println(httpResponse.body());
    }
}
