package vip.gpg123.kubernetes.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesCluster implements Serializable {

    private String name;

    private Cluster cluster;

    @Data
    public static class Cluster implements Serializable {

        private String server;

        @com.fasterxml.jackson.annotation.JsonProperty("certificate-authority-data")
        private String certificateAuthorityData;
    }
}
