package vip.gpg123.ai.service.impl;

import org.springframework.stereotype.Service;
import vip.gpg123.ai.domain.RequestMessage;
import vip.gpg123.ai.service.DeepSeekR1Service;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/1 1:31
 **/
@Service
public class DeepSeekR1ServiceImpl extends AbstractAiServiceImpl implements DeepSeekR1Service {

    private static final String model = "deepseek-r1";

    private static final String api = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    private static final String apiKey = "sk-37d9ef3dd4e74516b355f7ccba0f73cd";

    public DeepSeekR1ServiceImpl() {
        super(model, api, apiKey);
    }

    /**
     * 单轮对话
     *
     * @param messages 消息
     * @return r
     */
    @Override
    public Object sendQuestion(List<RequestMessage> messages) {
        return super.sendQuestion(messages);
    }

    /**
     * 多轮对话
     *
     * @param messages 消息
     * @return r
     */
    @Override
    public Object sendQuestions(List<RequestMessage> messages) {
        return super.sendQuestions(messages);
    }
}
