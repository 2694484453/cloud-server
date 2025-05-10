package vip.gpg123.kubernetes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * k8s服务主机信息表
 * @TableName kubernetes_server
 */
@TableName(value ="kubernetes_server")
@Data
public class KubernetesServer {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "cluster_name")
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
}
