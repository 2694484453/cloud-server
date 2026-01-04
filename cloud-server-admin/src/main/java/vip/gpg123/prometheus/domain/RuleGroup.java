package vip.gpg123.prometheus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class RuleGroup implements Serializable {

    private String name; // 规则组名称

    private String interval; // 评估间隔，如 "15s", "1m"

    private List<RuleFileProps> rules; // 规则列表

}
