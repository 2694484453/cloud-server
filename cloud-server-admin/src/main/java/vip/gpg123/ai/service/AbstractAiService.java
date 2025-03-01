package vip.gpg123.ai.service;

import vip.gpg123.ai.domain.RequestMessage;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/3/1 0:59
 **/
public interface AbstractAiService {

    /**
     * 单轮对话
     *
     * @param messages 消息
     * @return r
     */
    Object sendQuestion(List<RequestMessage> messages);

    /**
     * 多轮对话
     *
     * @param messages 消息
     * @return r
     */
    Object sendQuestions(List<RequestMessage> messages);
}
