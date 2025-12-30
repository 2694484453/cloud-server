package vip.gpg123.umami.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName website
 */
@TableName(value ="website")
@Data
public class Website implements Serializable {
    /**
     * 
     */
    @TableId(value = "website_id")
    private String websiteId;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "domain")
    private String domain;

    /**
     * 
     */
    @TableField(value = "share_id")
    private String shareId;

    /**
     * 
     */
    @TableField(value = "reset_at")
    private Date resetAt;

    /**
     * 
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     * 
     */
    @TableField(value = "deleted_at")
    private Date deletedAt;

    /**
     * 
     */
    @TableField(value = "created_by")
    private String createdBy;

    /**
     * 
     */
    @TableField(value = "team_id")
    private String teamId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}