package vip.gpg123.repo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChartApp implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 版本数量
     */
    private Integer versionCount;

    /**
     * 仓库地址
     */
    private String repoUrl;

    /**
     * 仓库名称
     */
    private String repoName;

    /**
     * 类型
     */
    private String type;

    /**
     * 索引时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String generated;

    /**
     * 版本列表
     */
    private Object items;
}
