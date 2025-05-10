package vip.gpg123.docker.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * docker仓库认证信息表
 * @TableName docker_repo
 */
@TableName(value ="docker_repo")
@Data
public class DockerRepo {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 仓库名称
     */
    @TableField(value = "repo_name")
    private String repoName;

    /**
     * 仓库类型
     */
    @TableField(value = "repo_type")
    private String repoType;

    /**
     * 主机ip/域名
     */
    @TableField(value = "repo_host")
    private String repoHost;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

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