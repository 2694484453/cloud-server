package vip.gpg123.prometheus.domain;

import lombok.Data;

import java.util.List;

@Data
public class PrometheusTargetData {

    private List<ActiveTarget> activeTargets;

    private Object droppedTargetCounts;

    private List<Object> droppedTargets;
}
