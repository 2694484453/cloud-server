package vip.gpg123.umami.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class UmamiUser implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 
     */
    @TableField(value = "username")
    private String username;

    /**
     * 
     */
    @TableField(value = "password")
    private String password;

    /**
     * 
     */
    @TableField(value = "role")
    private String role;

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
    @TableField(value = "display_name")
    private String displayName;

    /**
     * 
     */
    @TableField(value = "logo_url")
    private String logoUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}