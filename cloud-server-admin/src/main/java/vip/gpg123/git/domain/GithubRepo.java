package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GithubRepo implements Serializable {

    private String id;

    private String name;

    private String full_name;

    private String html_name;

    private String html_url;

    private String git_url;

    private String ssh_url;

    private String language;

    private String visibility;

    private String description;

    private String created_at;

    private String updated_at;

    private String pushed_at;

}
