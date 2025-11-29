package vip.gpg123.git.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * git认证信息
 * @TableName git_token
 */
@TableName(value ="git_token")
@Data
public class GitToken implements Serializable {
    /**
     * 
     */
    @TableId(value = "id",  type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 认证方式
     */
    @TableField(value = "source")
    private String source;

    /**
     * 主页
     */
    @TableField(value = "home_url")
    private String homeUrl;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * token
     */
    @TableField(value = "access_token")
    private String accessToken;

    /**
     * 标签
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}