package vip.gpg123.umami.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName website_event
 */
@TableName(value ="website_event")
@Data
public class WebsiteEvent implements Serializable {
    /**
     * 
     */
    @TableId(value = "event_id")
    private String eventId;

    /**
     * 
     */
    @TableField(value = "website_id")
    private String websiteId;

    /**
     * 
     */
    @TableField(value = "session_id")
    private String sessionId;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "url_path")
    private String urlPath;

    /**
     * 
     */
    @TableField(value = "url_query")
    private String urlQuery;

    /**
     * 
     */
    @TableField(value = "referrer_path")
    private String referrerPath;

    /**
     * 
     */
    @TableField(value = "referrer_query")
    private String referrerQuery;

    /**
     * 
     */
    @TableField(value = "referrer_domain")
    private String referrerDomain;

    /**
     * 
     */
    @TableField(value = "page_title")
    private String pageTitle;

    /**
     * 
     */
    @TableField(value = "event_type")
    private Integer eventType;

    /**
     * 
     */
    @TableField(value = "event_name")
    private String eventName;

    /**
     * 
     */
    @TableField(value = "visit_id")
    private String visitId;

    /**
     * 
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 
     */
    @TableField(value = "fbclid")
    private String fbclid;

    /**
     * 
     */
    @TableField(value = "gclid")
    private String gclid;

    /**
     * 
     */
    @TableField(value = "li_fat_id")
    private String liFatId;

    /**
     * 
     */
    @TableField(value = "msclkid")
    private String msclkid;

    /**
     * 
     */
    @TableField(value = "ttclid")
    private String ttclid;

    /**
     * 
     */
    @TableField(value = "twclid")
    private String twclid;

    /**
     * 
     */
    @TableField(value = "utm_campaign")
    private String utmCampaign;

    /**
     * 
     */
    @TableField(value = "utm_content")
    private String utmContent;

    /**
     * 
     */
    @TableField(value = "utm_medium")
    private String utmMedium;

    /**
     * 
     */
    @TableField(value = "utm_source")
    private String utmSource;

    /**
     * 
     */
    @TableField(value = "utm_term")
    private String utmTerm;

    /**
     * 
     */
    @TableField(value = "hostname")
    private String hostname;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}