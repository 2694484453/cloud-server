package vip.gpg123.platform.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class OverViewVo implements Serializable {

    /**
     * 域名
     */
    private Map<String, Object> domain;

    /**
     * 解析
     */
    private Map<String, Object> domainRecord;

    /**
     * 服务实例
     */
    private Map<String, Object> service;
}
