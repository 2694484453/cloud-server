import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.caddy.domain.CloudCaddy;
import vip.gpg123.caddy.service.CloudCaddyService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = CloudServerApplication.class)
public class CaddyTest {

    private static final String host = "http://ecs.gpg123.vip:2019";

    @Autowired
    private CloudCaddyService cloudCaddyService;

    /**
     * 获取当前配置
     */
    @Test
    public void getConfig() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.GET, host + "/config/");
        httpRequest.header("Content-Type", "application/json;charset=utf-8");
        HttpResponse httpResponse = httpRequest.execute();
        JSONObject jsonObject = JSONUtil.parseObj(httpResponse.body());
        JSONObject apps = jsonObject.getJSONObject("apps");
        JSONObject http = apps.getJSONObject("http");
        JSONObject servers = http.getJSONObject("servers");
        JSONObject srv0 = servers.getJSONObject("srv0");
        JSONArray routes = srv0.getJSONArray("routes");
        routes.forEach(e -> {
            JSONObject route = JSONUtil.parseObj(e.toString());
            JSONArray match = JSONUtil.parseArray(route.getJSONArray("match"));
            boolean status = (boolean) route.get("terminal");
            CloudCaddy cloudCaddy = new CloudCaddy();
            if (match.size() > 1) {
                List<String> domains = new ArrayList<>();
                match.forEach(m -> {
                    JSONObject object = JSONUtil.parseObj(m.toString());
                    JSONArray host = object.getJSONArray("host");
                    host.forEach(h -> {
                        domains.add(h.toString());
                    });
                });
                cloudCaddy.setDomain(StrUtil.join(",", domains));
                cloudCaddy.setName(domains.get(0));
            } else {
                JSONObject object = JSONUtil.parseObj(match.get(0));
                JSONArray host = object.getJSONArray("host");
                cloudCaddy.setDomain(host.get(0).toString());
                cloudCaddy.setName(host.get(0).toString());
            }
            cloudCaddy.setCreateBy("1");
            cloudCaddy.setCreateTime(DateUtil.date());
            cloudCaddy.setStatus(status ? "ok" : "fail");
            cloudCaddyService.save(cloudCaddy);
        });
    }

    @Test
    public void postConfig() {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, host + "/config/");
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
        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, host + "/adapt");
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
