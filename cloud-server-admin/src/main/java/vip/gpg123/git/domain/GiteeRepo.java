package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopuguang
 * @date 2024/11/30 3:59
 **/
@Data
public class GiteeRepo implements Serializable {

    private String id;

    private String name;

    private String full_name;

    private String human_name;

    private String language;

    private String status;

    private String description;

    private String html_url;

    private String ssh_url;

    private String created_at;

    private String updated_at;

    private String pushed_at;
}
