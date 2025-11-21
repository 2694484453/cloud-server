package vip.gpg123.kubernetes.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesUser{

    private String name;

    private User user;


    @Data
    static
    class User {

        @JsonProperty("client-certificate-data")
        private String clientCertificateData;

        @JsonProperty("client-key-data")
        private String clientKeyData;
    }
}
