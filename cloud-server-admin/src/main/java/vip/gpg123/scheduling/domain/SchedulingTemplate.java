package vip.gpg123.scheduling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 调度模版
 * @TableName scheduling_template
 */
@TableName(value ="scheduling_template")
@Data
public class SchedulingTemplate implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "job_type")
    private String jobType;

    /**
     *
     */
    @TableField(value = "bind_task")
    private String bindTask;

    /**
     *
     */
    @TableField(value = "bind_method")
    private String bindMethod;

    /**
     * 
     */
    @TableField(value = "bind_params", typeHandler = JacksonTypeHandler.class)
    private Object bindParams;

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