package vip.gpg123.prometheus.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class PrometheusConfigs implements Serializable {

    private List<String> targets;

    private Map<String,Object> labels;
}
