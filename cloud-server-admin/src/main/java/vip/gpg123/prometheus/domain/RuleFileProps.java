package vip.gpg123.prometheus.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RuleFileProps implements Serializable {

    // 通用字段
    private String alert; // 告警名称，如 "InstanceDown"

    private String expr; // PromQL 表达式

    private String forProperty; // 等待时间，如 "5m" (Java关键字for不能作为字段名，故加Property)

    // 标签 (会发送给 Alertmanager)
    private Map<String, Object> labels;

    // 注解 (详细信息)
    private Map<String, Object> annotations;

}
