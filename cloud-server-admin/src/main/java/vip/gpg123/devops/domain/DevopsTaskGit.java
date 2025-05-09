package vip.gpg123.devops.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName devops_task_git
 */
@TableName(value ="devops_task_git")
@Data
public class DevopsTaskGit implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 任务id
     */
    @TableField(value = "job_id")
    private String jobId;

    /**
     * 类型
     */
    @TableField(value = "task_git_type")
    private String taskGitType;

    /**
     * 名称
     */
    @TableField(value = "task_git_name")
    private String taskGitName;

    /**
     * 地址
     */
    @TableField(value = "task_git_url")
    private String taskGitUrl;

    /**
     * 分支
     */
    @TableField(value = "task_git_branch")
    private String taskGitBranch;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}