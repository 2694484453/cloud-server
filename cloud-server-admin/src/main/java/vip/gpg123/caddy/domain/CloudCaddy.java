package vip.gpg123.caddy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * caddy
 *
 * @TableName cloud_caddy
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "cloud_caddy")
@Data
public class CloudCaddy extends BaseEntity implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     *
     */
    @TableField(value = "domain")
    private String domain;

    /**
     *
     */
    @TableField(value = "status")
    private String status;

    /**
     * 配置内容
     */
    @TableField(value = "config", typeHandler = JacksonTypeHandler.class)
    private Object config;

    /**
     *
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