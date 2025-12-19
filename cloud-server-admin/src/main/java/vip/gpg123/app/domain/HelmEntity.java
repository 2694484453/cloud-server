package vip.gpg123.app.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class HelmEntity implements Serializable {

    private String chartUrl;

    private String chartName;

    private String chartValues;

    private String releaseName;

    private String nameSpace;

    private String kubeContext;

    private String description;

    private String icon;
}
