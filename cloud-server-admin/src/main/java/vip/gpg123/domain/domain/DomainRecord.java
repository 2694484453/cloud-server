package vip.gpg123.domain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 应用域名解析记录信息
 * @TableName domain_record
 */
@TableName(value ="domain_record")
@Data
public class DomainRecord implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 域名
     */
    @TableField(value = "domain_name")
    private String domainName;

    /**
     * 主机
     */
    @TableField(value = "domain_host")
    private String domainHost;

    /**
     * 类型
     */
    @TableField(value = "domain_type")
    private String domainType;

    /**
     * 值
     */
    @TableField(value = "domain_value")
    private String domainValue;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}