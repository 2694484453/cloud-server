package vip.gpg123.traefik.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 
 * @TableName traefik_proxy
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="traefik_proxy")
@Data
public class TraefikProxy extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 路由入口
     */
    @TableField(value = "entry_points")
    private String entryPoints;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 域名
     */
    @TableField(value = "domains")
    private String domains;

    /**
     * tls名称
     */
    @TableField(value = "tls_name")
    private String tlsName;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 标签
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 服务
     */
    @TableField(value = "server_urls")
    private String serverUrls;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

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