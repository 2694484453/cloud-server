package vip.gpg123.discovery.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class NacosConfigItem implements Serializable {

    private String appName;

    private String content;

    private String dataId;

    private String encryptedDataKey;

    private String group;

    private String id;

    private String md5;

    private String tenant;

    private String type;
}
