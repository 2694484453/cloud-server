package vip.gpg123.git.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class GitlabRepo implements Serializable {

    private String id;

    private String name;

    private String description;
}
