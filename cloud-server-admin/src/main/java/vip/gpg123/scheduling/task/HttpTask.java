package vip.gpg123.scheduling.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component(value = "httpTask")
public class HttpTask {

    /**
     * request
     *
     * @param url       url
     * @param params    p
     * @param basicAuth ba
     * @param token     t
     * @return r
     */
    public String request(String method, String url, JsonObject body, Map<String, String> params, Map<String, String> basicAuth, Map<String, String> header, String token) {
        HttpRequest httpRequest = HttpUtil.createRequest(Method.valueOf(method), url);
        httpRequest.charset(StandardCharsets.UTF_8);
        httpRequest.header("Accept", "application/json");
        httpRequest.header("Content-Type", "application/json");
        httpRequest.formStr(params);
        httpRequest.bearerAuth(token);
        httpRequest.body(body.toString());
        httpRequest.basicAuth(basicAuth.get("username"), basicAuth.get("password"));
        httpRequest.headerMap(header, true);
        HttpResponse response = httpRequest.execute();
        return response.body();
    }

}
