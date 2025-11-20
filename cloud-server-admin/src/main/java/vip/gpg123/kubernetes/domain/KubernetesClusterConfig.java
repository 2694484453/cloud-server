package vip.gpg123.kubernetes.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesClusterConfig implements Serializable {

    private String name;

    private Cluster cluster;

    @Data
    public static class Cluster implements Serializable {

        private String server;

        @JsonProperty("certificate-authority-data")
        private String certificateAuthorityData;
    }
}
