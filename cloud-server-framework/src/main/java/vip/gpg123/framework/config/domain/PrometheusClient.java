package vip.gpg123.framework.config.domain;

import lombok.Data;

/**
 * @author gaopuguang_zz
 * @version 1.0
 * @description: TODO
 * @date 2025/2/14 14:35
 */
@Data
public class PrometheusClient {

    /**
     * 版本
     */
    private String version;

    /**
     * 端点
     */
    private String endpoint;
}
