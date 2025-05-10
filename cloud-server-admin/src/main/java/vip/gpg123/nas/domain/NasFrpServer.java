package vip.gpg123.nas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName nas_frp_server
 */
@TableName(value ="nas_frp_server")
@Data
public class NasFrpServer implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "server_id")
    private String serverId;

    /**
     * 名称
     */
    @TableField(value = "server_name")
    private String serverName;

    /**
     * ip
     */
    @TableField(value = "server_ip")
    private String serverIp;

    /**
     * 端口
     */
    @TableField(value = "server_port")
    private Integer serverPort;

    /**
     * 认证类型
     */
    @TableField(value = "auth_method")
    private String authMethod;

    /**
     * 认证token
     */
    @TableField(value = "auth_token")
    private String authToken;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}