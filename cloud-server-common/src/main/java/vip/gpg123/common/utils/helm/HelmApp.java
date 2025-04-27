package vip.gpg123.common.utils.helm;

import lombok.Data;

@Data
public class HelmApp {

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用空间
     */
    private String namespace;

    /**
     * 应用版本
     */
    private String revision;

    /**
     * 应用更新时间
     */
    private String updated;

    /**
     * 应用状态
     */
    private String status;

    /**
     * 应用版本
     */
    private String app_version;

    /**
     * 应用chart
     */
    private String chart;
}
