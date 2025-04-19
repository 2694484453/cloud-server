package vip.gpg123.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Email implements Serializable {

    /**
     * 收件人列表
     */
    private String[] tos;
    /**
     * 抄送人列表（carbon copy）
     */
    private String[] ccs;
    /**
     * 密送人列表（blind carbon copy）
     */
    private String[] bccs;
    /**
     * 回复地址(reply-to)
     */
    private String[] reply;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
}
