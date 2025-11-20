package vip.gpg123.kubernetes.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KubernetesFileConfig implements Serializable {

    private String apiVersion;

    private List<KubernetesClusterConfig> clusters;

    private List<KubernetesContext> contexts;

    @com.fasterxml.jackson.annotation.JsonProperty("current-context")
    private String currentContext;

    private String kind;

    private Object preferences;

    private List<KubernetesUser> users;

}
