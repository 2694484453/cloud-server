package vip.gpg123.platform.domain;

import lombok.Data;

import java.util.List;

@Data
public class PrometheusTargetData {

    private List<ActiveTarget> activeTargets;

    private Object droppedTargetCounts;

    private List<String> droppedTargets;
}
