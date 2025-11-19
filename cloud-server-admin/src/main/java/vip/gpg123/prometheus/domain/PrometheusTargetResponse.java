package vip.gpg123.prometheus.domain;

import lombok.Data;
import vip.gpg123.platform.domain.PrometheusTargetData;

import java.io.Serializable;

@Data
public class PrometheusTargetResponse implements Serializable {

    private String status;

    private PrometheusTargetData data;
}
