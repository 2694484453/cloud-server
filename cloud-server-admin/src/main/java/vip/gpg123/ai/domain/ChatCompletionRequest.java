package vip.gpg123.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaopuguang
 * @date 2025/1/29 3:53
 **/
@Data
public class ChatCompletionRequest {

    private String model;

    private List<RequestMessage> messages;



}
