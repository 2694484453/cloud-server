package vip.gpg123.devops.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 定时任务
 * @TableName devops_cron_job
 */
@TableName(value ="devops_cron_job")
@Data
public class DevopsCronJob implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 标签
     */
    @TableField(value = "labels")
    private String labels;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 环境变量
     */
    @TableField(value = "env")
    private String env;

    /**
     * 表达式
     */
    @TableField(value = "cron")
    private String cron;

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

    /**
     * 配置内容
     */
    @TableField(value = "content")
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}