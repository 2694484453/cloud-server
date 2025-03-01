package vip.gpg123.ai;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.domain.RequestMessage;
import vip.gpg123.ai.service.DeepSeekR1Service;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/2/15 14:48
 **/
@RestController
@RequestMapping("/ai/deepSeek")
@Api(tags = "【ai】deepSeekR1大模型")
public class DeepSeekR1Controller {

    @Autowired
    private DeepSeekR1Service deepSeekR1Service;

    /**
     * 发送测试
     *
     * @return r
     */
    @PostMapping("/demo")
    @ApiOperation(value = "测试")
    public AjaxResult sendMessageDemo() {
        // 设置消息
        List<RequestMessage> messages = new ArrayList<RequestMessage>() {{
            new RequestMessage("system", "欢迎");
            new RequestMessage("user", "你是谁");
        }};
        Object result = deepSeekR1Service.sendQuestion(messages);
        return AjaxResult.success(result);
    }


    /**
     * 发送
     *
     * @return r
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送")
    public AjaxResult sendMessage() {
        // 设置消息
        List<RequestMessage> messages = new ArrayList<>();
        messages.add(new RequestMessage("user", "9.9和9.11谁大"));
        Object result = deepSeekR1Service.sendQuestion(messages);
        return AjaxResult.success(result);
    }
}
