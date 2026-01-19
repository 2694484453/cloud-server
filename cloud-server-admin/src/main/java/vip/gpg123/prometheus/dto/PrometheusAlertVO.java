package vip.gpg123.prometheus.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.prometheus.domain.PrometheusAlert;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrometheusAlertVO extends PrometheusAlert {

    private String groupName;

    private String exporterType;

}
