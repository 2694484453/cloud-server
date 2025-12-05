package vip.gpg123.devops.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Build implements Serializable {

    private String imageName;

    private String imageVersion;


}
