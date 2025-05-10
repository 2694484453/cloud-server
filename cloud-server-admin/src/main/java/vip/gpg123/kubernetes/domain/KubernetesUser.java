package vip.gpg123.kubernetes.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesUser implements Serializable {

    private String name;

    private User user;


    @Data
    public static class User implements Serializable {

        @com.fasterxml.jackson.annotation.JsonProperty("client-certificate-data")
        private String clientCertificateData;

        @com.fasterxml.jackson.annotation.JsonProperty("client-key-data")
        private String clientKeyData;
    }
}
