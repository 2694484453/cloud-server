package vip.gpg123.repo.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HelmRepoResponse implements Serializable {

    /**
     * apiVersion
     */
    private String apiVersion;

    /**
     * name
     */
    private String name;

    /**
     * entries
     */
    private Map<String, List<HelmRepoItem>> entries;

    /**
     * 生成
     */
    private String generated;
}
