package vip.gpg123.devops.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务构建信息表
 * @TableName devops_task_build
 */
@TableName(value ="devops_task_build")
@Data
public class DevopsTaskBuild implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "task_build_id")
    private String taskBuildId;

    /**
     * 任务id
     */
    @TableField(value = "job_id")
    private String jobId;

    /**
     * 类型
     */
    @TableField(value = "task_build_type")
    private String taskBuildType;

    /**
     * 名称
     */
    @TableField(value = "task_build_name")
    private String taskBuildName;

    /**
     * 镜像
     */
    @TableField(value = "task_build_image")
    private String taskBuildImage;

    /**
     * 镜像来源
     */
    @TableField(value = "image_source")
    private String imageSource;

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