package vip.gpg123.prometheus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 
 * @TableName prometheus_target
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="prometheus_target")
@Data
public class PrometheusTarget extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "targetId", type = IdType.AUTO)
    private Integer targetId;

    /**
     * 应用名称
     */
    @TableField(value = "job_name")
    private String jobName;

    /**
     * 端点地址
     */
    @TableField(value = "targets")
    private String targets;

    /**
     * path
     */
    @TableField(value = "metrics_path")
    private String metricsPath;

    /**
     * type
     */
    @TableField(value = "scheme_type")
    private String schemeType;

    /**
     *
     */
    @TableField(value = "scrape_interval")
    private Integer scrapeInterval;

    /**
     *
     */
    @TableField(value = "scrape_timeout")
    private Integer scrapeTimeout;

    /**
     * labels
     */
    @TableField(value = "labels", typeHandler = JacksonTypeHandler.class)
    private Object labels;

    /**
     * url
     */
    @TableField(value = "global_url")
    private String globalUrl;

    /**
     * exporter类型
     */
    @TableField(value = "exporter_type")
    private String exporterType;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     *
     */
    @TableField(value = "error_reason")
    private String errorReason;

    /**
     * 
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}