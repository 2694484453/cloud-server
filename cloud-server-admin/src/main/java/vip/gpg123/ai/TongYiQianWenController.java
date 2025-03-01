package vip.gpg123.ai;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.config.AiWebSocketConfig;
import vip.gpg123.ai.domain.RequestMessage;
import vip.gpg123.ai.service.TongYiQianWenAiService;
import vip.gpg123.common.core.domain.AjaxResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:00
 **/
@RestController
@RequestMapping("/ai/tyqw")
@Api(tags = "【ai】通义千问大模型")
public class TongYiQianWenController {

    @Autowired
    private TongYiQianWenAiService tongYiQianWenAiService;

    @Autowired
    private AiWebSocketConfig aiWebSocketConfig;

    /**
     * 发送消息
     *
     * @param id     id
     * @param messages m
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送")
    public AjaxResult send(@RequestParam(value = "id",required = false) String id, @RequestBody List<RequestMessage> messages) {
        // 设置消息
        if (ObjectUtil.isNull(messages)) {
            throw new RuntimeException("消息不能为空！");
        }
        Object result = tongYiQianWenAiService.sendQuestion(messages);
        return AjaxResult.success(result);
    }
}
