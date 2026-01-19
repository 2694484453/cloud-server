package vip.gpg123.prometheus.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.prometheus.domain.PrometheusRule;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrometheusRuleVO extends PrometheusRule {

    private String groupName;
}
