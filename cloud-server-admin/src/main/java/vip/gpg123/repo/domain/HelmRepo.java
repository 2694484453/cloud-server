package vip.gpg123.repo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HelmRepo implements Serializable {

    /**
     * api版本
     */
    private String apiVersion;

    /**
     * 名称
     */
    private String name;

    /**
     * url
     */
    private List<String> urls;

    /**
     * 版本
     */
    private String version;

    /**
     * 图标
     */
    private String icon;

    /**
     * 创建时间
     */
    private String created;

    /**
     * 签名
     */
    private String digest;
}
