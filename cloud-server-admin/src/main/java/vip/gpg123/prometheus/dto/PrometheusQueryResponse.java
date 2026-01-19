package vip.gpg123.prometheus.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrometheusQueryResponse implements Serializable {

    private String status;

    private String error;

    private String errorType;

}
