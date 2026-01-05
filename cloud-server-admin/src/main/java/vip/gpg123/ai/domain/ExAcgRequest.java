package vip.gpg123.ai.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExAcgRequest implements Serializable {

    private String prompt;

    private String negative_prompt;

    private Integer width;

    private Integer height;

    private Integer steps;

    private Float cfg;

    private Integer model_index;

    private Integer seed;

}
