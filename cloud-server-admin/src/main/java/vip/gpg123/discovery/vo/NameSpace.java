package vip.gpg123.discovery.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class NameSpace implements Serializable {

    private String namespace;

    private String namespaceShowName;

    private String namespaceDesc;

    private Integer quota;

    private Integer configCount;

    private Integer type;
}
