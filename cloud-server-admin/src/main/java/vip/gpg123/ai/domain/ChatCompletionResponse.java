package vip.gpg123.ai.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionResponse {

    private List<Choice> choices;

    private String object;

    private Usage usage;

    private long created;

    private String model;

    private String id;
}

@Data
class Choice {

    private Message message;

    private String finishReason;

    private int index;

    private Object logprobs;
}

@Data
class Message {
    private String content;

    private String reasoningContent;

    private String role;
}

@Data
class Usage {

    private int promptTokens;

    private int completionTokens;

    private int totalTokens;
}
