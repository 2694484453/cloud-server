package com.ruoyi.web.controller.build.domain;

import lombok.Data;

/**
 * @author gaopuguang
 * @date 2024/11/9 23:30
 **/
@Data
public class Namespace {

    private Long id;

    private String type;

    private String name;

    private String path;

    private String html_url;
}
