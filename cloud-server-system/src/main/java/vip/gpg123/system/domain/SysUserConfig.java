package vip.gpg123.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName sys_user_config
 */
@TableName(value ="sys_user_config")
@Data
public class SysUserConfig implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "user_id")
    private String userId;

    /**
     * 配置内容
     */
    @TableField(value = "config")
    private String config;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}