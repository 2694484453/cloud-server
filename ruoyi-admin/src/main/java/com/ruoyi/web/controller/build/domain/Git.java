package com.ruoyi.web.controller.build.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:55
 **/
@Data
public class Git implements Serializable {

    private Long gitId;

    private String gitName;

    private String home;

    private String httpUrl;

    private String sshUrl;

    private String language;

    private String type;

    private String createTime;

    private String updateTime;

    private String pushedTime;
}
