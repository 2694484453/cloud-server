package vip.gpg123.kubernetes.domain;

import lombok.Data;

import java.util.List;

@Data
public class KubernetesContext {

    private String name;

    private List<context> context;

    static class context{

        String cluster;

        String user;
    }
}
