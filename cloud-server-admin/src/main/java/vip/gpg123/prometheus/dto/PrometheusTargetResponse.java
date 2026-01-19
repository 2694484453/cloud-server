package vip.gpg123.prometheus.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrometheusTargetResponse implements Serializable {

    private String status;

    private PrometheusTargetData data;
}
