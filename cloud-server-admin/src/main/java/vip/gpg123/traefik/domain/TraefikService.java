package vip.gpg123.traefik.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class TraefikService implements Serializable {

    private TraefikLoadBalancer traefikLoadBalancer;

    private Map<String, String> serverStatus;

    private String status;

    private String name;

    private String provider;

    private String type;

    private List<String> usedBy;

}
