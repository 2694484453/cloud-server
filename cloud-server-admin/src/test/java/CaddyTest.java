import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

public class CaddyTest {

    private static final String host = "http://hongkong.gpg123.vip:2020";

    /**
     * 获取当前配置
     */
    @Test
    public void getConfig() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.GET, host + "/config/");
        httpRequest.header("Content-Type", "application/json;charset=utf-8");
        HttpResponse httpResponse = httpRequest.execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        String str = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonObject));
        System.out.println(str);
    }

    @Test
    public void postConfig() {

    }

    @Test
    public void stop() {

    }

    @Test
    public void load() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, host + "/load/");
        HttpResponse httpResponse = httpRequest.execute();
    }

    @Test
    public void log() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.GET, host + "/log/stream");
        HttpResponse httpResponse = httpRequest.execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        String str = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonObject));
        System.out.println(str);
    }
}
