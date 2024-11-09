package com.ruoyi.web.controller.build.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:28
 **/
@Data
public class GiteeRepo {

    private Long id;

    private String full_name;

    private String human_name;

    private String url;

    private Namespace namespace;

    private String path;

    private String name;

    private Owner owner;

    private Assigner assigner;

    private String description;

    @JsonProperty("private")
    private Boolean private_;

    @JsonProperty("public")
    private Boolean public_;

    private Boolean internal;

    private Boolean fork;

    private String html_url;

    private String ssh_url;

    private String forks_url;

    private String keys_url;

    private String collaborators_url;

    private String hooks_url;

    private String branches_url;

    private String tags_url;

    private String blobs_url;

    private String stargazers_url;

    private String language;

    private String pushed_at;

    private String created_at;

    private String updated_at;

    private String relation;
}
