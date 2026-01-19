package vip.gpg123.prometheus.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class AlertDTO implements Serializable {

    private String status; // 状态: firing (触发) 或 resolved (恢复)

    private Map<String, String> labels; // 标签: 包含 alertname, instance, severity 等

    private Map<String, String> annotations; // 注解: 包含 summary, description 等

    private String startsAt;

    private String endsAt;

    private String generatorURL;

    private String fingerprint;

}
