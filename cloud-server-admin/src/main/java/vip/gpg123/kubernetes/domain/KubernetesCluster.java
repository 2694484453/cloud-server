package vip.gpg123.kubernetes.domain;

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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * k8s服务主机信息表
 * @TableName kubernetes_server
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="kubernetes_cluster")
@Data
public class KubernetesCluster extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "cluster_name")
    @NotBlank(message = "集群名称不能为空")
    private String clusterName;

    /**
     * 用户
     */
    @TableField(value = "cluster_owner")
    private String clusterOwner;

    /**
     * 上下文名称
     */
    @TableField(value = "context_name")
    private String contextName;

    /**
     * 配置内容
     */
    @TableField(value = "config")
    private String config;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
