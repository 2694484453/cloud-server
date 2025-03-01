package vip.gpg123.ai.service.impl;

import org.springframework.stereotype.Service;
import vip.gpg123.ai.domain.RequestMessage;
import vip.gpg123.ai.service.TongYiQianWenAiService;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/2 0:26
 **/
@Service
public class TongYiQianWenAiServiceImpl extends AbstractAiServiceImpl implements TongYiQianWenAiService {

    private static final String model = "qwen-max";

    private static final String apiKey = "sk-37d9ef3dd4e74516b355f7ccba0f73cd";

    private static final String api = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    public TongYiQianWenAiServiceImpl() {
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
