package vip.gpg123.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vip.gpg123.ai.domain.ChatCompletionRequest;
import vip.gpg123.ai.domain.ChatCompletionResponse;
import vip.gpg123.ai.domain.RequestMessage;
import vip.gpg123.ai.service.AbstractAiService;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/1 1:04
 **/
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAiServiceImpl implements AbstractAiService {

    private String model;

    private String api;

    private String apiKey;

    /**
     * 单轮对话
     *
     * @param messages 消息
     * @return r
     */
    @Override
    public Object sendQuestion(List<RequestMessage> messages) {
        return sendHttpRequest(messages, model, api, apiKey);
    }

    /**
     * 多轮对话
     *
     * @param messages 消息
     * @return r
     */
    @Override
    public Object sendQuestions(List<RequestMessage> messages) {
        return sendHttpRequest(messages, model, api, apiKey);
    }

    /**
     * http调用
     *
     * @param model  模型名称
     * @param api    api地址
     * @param apiKey key
     * @return r
     */
    private Object sendHttpRequest(List<RequestMessage> messages, String model, String api, String apiKey) {
        ChatCompletionResponse chatCompletionResponse;
        try {
            //
            StrUtil.blankToDefault(model, "deepseek-r1");
            StrUtil.blankToDefault(api, "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions");
            if (StrUtil.isBlankIfStr(apiKey)) {
                throw new RuntimeException("apiKey不能为空！");
            }
            ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
            chatCompletionRequest.setMessages(messages);
            chatCompletionRequest.setModel(model);
            //
            HttpResponse httpResponse = HttpUtil.createPost(api)
                    .body(JSONUtil.toJsonStr(chatCompletionRequest))
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .disableCache()
                    .execute();
            JSONObject result = JSONUtil.parseObj(httpResponse.body());
            chatCompletionResponse = JSONUtil.toBean(result.toString(), ChatCompletionResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chatCompletionResponse;
    }
}
