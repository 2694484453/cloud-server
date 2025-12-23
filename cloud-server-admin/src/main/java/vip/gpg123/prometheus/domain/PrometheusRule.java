package vip.gpg123.prometheus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 
 * @TableName prometheus_rule
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="prometheus_rule")
@Data
public class PrometheusRule extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 
     */
    @TableField(value = "alert_name")
    private String alertName;

    /**
     * 告警状态
     */
    @TableField(value = "alert_status")
    private String alertStatus;

    /**
     * 
     */
    @TableField(value = "labels", typeHandler = JacksonTypeHandler.class)
    private Object labels;

    /**
     * 
     */
    @TableField(value = "for_time")
    private String forTime;

    /**
     * 
     */
    @TableField(value = "expr")
    private String expr;

    /**
     * 
     */
    @TableField(value = "annotations", typeHandler = JacksonTypeHandler.class)
    private Object annotations;

    /**
     * 
     */
    @TableField(value = "type")
    private String type;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
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
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}