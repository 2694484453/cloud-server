package vip.gpg123.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 通知公告表 sys_notice
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_notice")
public class SysNotice extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    @TableId("notice_id")
    private Long noticeId;

    /** 公告标题 */
    @TableField("notice_title")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @TableField("notice_type")
    private String noticeType;

    /** 公告内容 */
    @TableField("notice_content")
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    @TableField("status")
    private String status;

}
