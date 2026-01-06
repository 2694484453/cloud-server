package vip.gpg123.ai.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ZImageRequest implements Serializable {

    private String model;

    private Input input;

    private Parameters parameters;

    @Data
    public static class Input {
        private List<Message> messages;
    }

    @Data
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Data
    public static class Content {
        private String text;
    }

    @Data
    public static class Parameters {
        private Boolean prompt_extend;
        private String size; // 例如 "1120*1440"
    }

}
