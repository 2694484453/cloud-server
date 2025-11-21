package vip.gpg123.kubernetes.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KubernetesFileConfig implements Serializable {

    private String apiVersion;

    private List<KubernetesClusterConfig> clusters;

    private List<KubernetesContext> contexts;

    @JsonProperty("current-context")
    private String currentContext;

    private String kind;

    private Object preferences;

    private List<KubernetesUser> users;

}
