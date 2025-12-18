package vip.gpg123.dashboard.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName session
 */
@TableName(value ="session")
@Data
public class Session implements Serializable {
    /**
     * 
     */
    @TableId(value = "session_id")
    private String sessionId;

    /**
     * 
     */
    @TableField(value = "website_id")
    private String websiteId;

    /**
     * 
     */
    @TableField(value = "browser")
    private String browser;

    /**
     * 
     */
    @TableField(value = "os")
    private String os;

    /**
     * 
     */
    @TableField(value = "device")
    private String device;

    /**
     * 
     */
    @TableField(value = "screen")
    private String screen;

    /**
     * 
     */
    @TableField(value = "language")
    private String language;

    /**
     * 
     */
    @TableField(value = "country")
    private String country;

    /**
     * 
     */
    @TableField(value = "region")
    private String region;

    /**
     * 
     */
    @TableField(value = "city")
    private String city;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "distinct_id")
    private String distinctId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}