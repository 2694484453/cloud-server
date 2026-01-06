package vip.gpg123.ai.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExAcgResponse implements Serializable {

    private String success;

    private String message;

    private Data data;

    @lombok.Data
    public static class Data {

        private String image_url;

        private String image_id;

        private String model_name;

        private Integer points_used;

        private Integer remaining_points;

    }
}
