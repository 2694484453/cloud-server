package vip.gpg123.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeBody implements Serializable {

    /**
     * 名称
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 发送对象
     */
    private String toUser;

    /**
     * 发送地址
     */
    private String toAddress;
}
