package vip.gpg123.notice.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 操作消息通知
 * @TableName sys_action_notice
 */
@TableName(value ="sys_action_notice")
@Data
public class SysActionNotice implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 通知类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 发送渠道
     */
    @TableField(value = "send_type")
    private String send_type;

    /**
     * 发送对象
     */
    @TableField(value = "to_user")
    private String to_user;

    /**
     * 发送地址
     */
    @TableField(value = "to_address")
    private String to_address;

    /**
     * 是否确认
     */
    @TableField(value = "is_confirm")
    private Integer is_confirm;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String create_by;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String update_by;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}