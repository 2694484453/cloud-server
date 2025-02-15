package vip.gpg123.ai;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.config.AiWebSocketConfig;
import vip.gpg123.ai.domain.Messages;
import vip.gpg123.ai.domain.TyParams;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:00
 **/
@RestController
@RequestMapping("/ai/tyqw")
@Api(tags = "【ai】通义千问大模型")
public class TongYiQianWenController {

    @Value("${ai.tyqw.api-key}")
    private String apiKey;

    @Value("${ai.tyqw.http}")
    private String http;

    @Autowired
    private AiWebSocketConfig aiWebSocketConfig;

    /**
     * 发送消息
     *
     * @param id     id
     * @param params p
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送")
    public void send(@RequestParam("id") String id, @RequestBody Map<String, String> params) {
        String message = "";
        HttpResponse httpResponse;
        // http
        try {
            //
            TyParams tyParams = new TyParams();
            tyParams.setModel("qwen-max");
            tyParams.setMessages(new ArrayList<Messages>() {{
                new Messages("system","欢迎");
                new Messages("user","你是谁");
            }});
            //
            httpResponse = HttpUtil.createPost(http)
                    .body(JSONUtil.toJsonStr(tyParams))
                    .contentType("application/json")
                    .header("Authorization","Bearer "+apiKey)
                    .disableCache()
                    .timeout(1000)
                    .execute();
            JSONObject result = JSONUtil.parseObj(httpResponse.body());
            aiWebSocketConfig.sendMessageToClient(id, result.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
