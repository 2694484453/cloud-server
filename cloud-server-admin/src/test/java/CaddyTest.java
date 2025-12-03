import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CaddyTest {

    private static final String host = "http://ecs.gpg123.vip:2019";

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
        HttpRequest  httpRequest = HttpUtil.createRequest(Method.POST, host + "/config/");
        httpRequest.header("Content-Type", "application/json;charset=utf-8");
        httpRequest.body(jsonObject().toString());
        HttpResponse httpResponse = httpRequest.execute();
        System.out.println(httpResponse.body());
    }

    /**
     * 验证配置：/adapt
     */
    @Test
    public void adapt() {
        HttpRequest  httpRequest = HttpUtil.createRequest(Method.POST, host + "/adapt");
        httpRequest.header("Content-Type", "application/json;charset=utf-8");
        HttpResponse httpResponse = httpRequest.execute();
        System.out.println(httpResponse.body());
    }

    @Test
    public void stop() {

    }

    @Test
    public void load() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, host + "/load");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(jsonObject().toString());
        HttpResponse httpResponse = httpRequest.execute();
        System.out.println(httpResponse.body());
    }

    @Test
    public void log() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.GET, host + "/log/stream");
        HttpResponse httpResponse = httpRequest.execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        String str = JSONUtil.formatJsonStr(JSONUtil.toJsonStr(jsonObject));
        System.out.println(str);
    }

    public JSONObject jsonObject() {
        String file = "/Volumes/gaopuguang/project/cloud-server/cloud-server-admin/src/test/java/demo.json";
        return JSONUtil.readJSONObject(new File(file), StandardCharsets.UTF_8);
    }
}
