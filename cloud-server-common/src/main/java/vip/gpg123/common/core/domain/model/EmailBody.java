package vip.gpg123.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Multipart;
import java.io.Serializable;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EmailBody implements Serializable {

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

    /**
     * 是否为HTML
     */
    private boolean isHtml;

    /**
     * 正文、附件和图片的混合部分
     */
    private final Multipart multipart;
}
