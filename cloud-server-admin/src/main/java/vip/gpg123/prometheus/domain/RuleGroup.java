package vip.gpg123.prometheus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RuleGroup implements Serializable {

    private String name; // 规则组名称

    private List<RuleFileProps> rules; // 规则列表

}
