package vip.gpg123.prometheus.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AlertManagerDTO implements Serializable {

    private String version;

    private String groupKey;

    private String status; // firing / resolved

    private List<AlertDTO> alerts; // 告警列表
}
