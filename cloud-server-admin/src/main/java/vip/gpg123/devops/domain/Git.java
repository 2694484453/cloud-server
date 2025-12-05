package vip.gpg123.devops.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Git implements Serializable {

    private String name;

    private String url;

    private String type;

    private String branch;

}
