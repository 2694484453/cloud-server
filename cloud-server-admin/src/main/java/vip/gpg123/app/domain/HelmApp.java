package vip.gpg123.app.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 
 * @TableName helm_app
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="helm_app")
@Data
public class HelmApp extends BaseEntity implements Serializable {
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
     * chart地址
     */
    @TableField(value = "chart_url")
    private String chartUrl;

    /**
     *
     */
    @TableField(value = "icon")
    private String icon;

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
    @TableField(value = "chart_values")
    private String chartValues;

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
    @TableField(value = "kube_Context")
    private String kubeContext;

    /**
     * 
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}