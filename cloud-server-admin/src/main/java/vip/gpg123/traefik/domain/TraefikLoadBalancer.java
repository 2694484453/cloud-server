package vip.gpg123.traefik.domain;

import cn.hutool.json.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TraefikLoadBalancer implements Serializable {

    private Boolean passHostHeader;

    private Map<String,Object> responseForwarding;

    private JSONArray servers;

    private String strategy;
}
