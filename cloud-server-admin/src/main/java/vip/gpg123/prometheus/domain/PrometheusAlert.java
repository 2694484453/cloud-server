package vip.gpg123.prometheus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 告警消息
 * @TableName prometheus_alert
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="prometheus_alert")
@Data
public class PrometheusAlert extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "alert_name")
    private String alertName;

    /**
     * 告警等级
     */
    @TableField(value = "alert_level")
    private String alertLevel;

    /**
     * 产生于
     */
    @TableField(value = "group_id")
    private String groupId;

    /**
     *
     */
    @TableField(value = "type")
    private String type;

    /**
     * 
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     *
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}