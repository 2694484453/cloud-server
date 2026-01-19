package vip.gpg123.prometheus.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrometheusBaseResponse implements Serializable {

    private String status;

    private Object data;
}
