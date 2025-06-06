package vip.gpg123.prometheus.domain;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrometheusTargetResponse implements Serializable {

    private String status;

    private JSONObject data;
}
