package vip.gpg123.scheduling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 定时任务调度日志表
 * @TableName sys_job_log
 */
@TableName(value ="sys_job_log")
@Data
public class SysJobLog implements Serializable {
    /**
     * 任务日志ID
     */
    @TableId(value = "job_log_id", type = IdType.AUTO)
    private Long jobLogId;

    /**
     * 任务名称
     */
    @TableField(value = "job_name")
    private String jobName;

    /**
     * 任务组名
     */
    @TableField(value = "job_group")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @TableField(value = "invoke_target")
    private String invokeTarget;

    /**
     * 日志信息
     */
    @TableField(value = "job_message")
    private String jobMessage;

    /**
     * 结果信息
     */
    @TableField(value = "result_info")
    private String resultInfo;

    /**
     * 执行状态（0正常 1失败）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 异常信息
     */
    @TableField(value = "exception_info")
    private String exceptionInfo;

    /**
     * 创建时间
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