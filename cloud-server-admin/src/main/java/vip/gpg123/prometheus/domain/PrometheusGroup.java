package vip.gpg123.prometheus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName prometheus_group
 */
@TableName(value ="prometheus_group")
@Data
public class PrometheusGroup implements Serializable {
    /**
     * 
     */
    @TableId(value = "group_id", type = IdType.AUTO)
    private Integer groupId;

    /**
     * 
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     *
     */
    @TableField(value = "interval")
    private String interval;

    /**
     * 
     */
    @TableField(value = "group_file_path")
    private String groupFilePath;

    /**
     * 
     */
    @TableField(value = "evaluation_time")
    private BigDecimal evaluationTime;

    /**
     * 
     */
    @TableField(value = "last_evaluation")
    private Date lastEvaluation;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}