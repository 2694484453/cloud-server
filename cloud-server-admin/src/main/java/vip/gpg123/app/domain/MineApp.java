package vip.gpg123.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName helm_app
 */
@TableName(value ="helm_app")
@Data
public class MineApp implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 应用名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * chart名称
     */
    @TableField(value = "chart_name")
    private String chartName;

    /**
     * git仓库地址
     */
    @TableField(value = "git_url")
    private String gitUrl;

    /**
     * 来源
     */
    @TableField(value = "source")
    private String source;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 安装结果
     */
    @TableField(value = "result")
    private String result;

    /**
     * 参数
     */
    @TableField(value = "value")
    private String value;

    /**
     * 标签
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 命名空间
     */
    @TableField(value = "name_space")
    private String nameSpace;

    /**
     * 发布名称
     */
    @TableField(value = "release_name")
    private String releaseName;

    /**
     * 
     */
    @TableField(value = "cluster_name")
    private String clusterName;

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