package vip.gpg123.ide.domain;

import lombok.Data;

/**
 * @author gaopuguang
 * @date 2025/2/16 18:43
 **/
@Data
public class IdeCodeOpen {

    /**
     * 名称
     */
    private String name;

    /**
     * http地址
     */
    private String htmlUrl;

    /**
     * 分支
     */
    private String branch;

    /**
     * 备注
     */
    private String description;
}
