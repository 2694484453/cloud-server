package vip.gpg123.common.core.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoticeBody implements Serializable {

    /**
     * 模块名称
     */
    private String modelName;

    /**
     * 操作
     */
    private String action;

    /**
     * 操作结果
     */
    private Boolean result;

    private String userName;

    private String userId;
}
