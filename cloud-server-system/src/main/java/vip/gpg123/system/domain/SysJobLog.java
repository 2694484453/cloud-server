package vip.gpg123.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vip.gpg123.common.annotation.Excel;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author gpg123
 */
@Data
@TableName(value = "sys_job_log")
public class SysJobLog implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "日志序号")
    @TableId(value = "job_log_id", type = IdType.AUTO)
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务类型 */
    @Excel(name = "任务类型")
    private String jobType;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
    @Excel(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /**
     * 结果信息
     */
    @TableField(value = "result_info")
    private String resultInfo;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /** 开始时间 */
    @TableField(exist = false)
    private Date startTime;

    /** 停止时间 */
    @TableField(exist = false)
    private Date stopTime;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @TableField(exist = false)
    private String remark;
}
