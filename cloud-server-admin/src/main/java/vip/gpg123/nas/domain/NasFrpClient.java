package vip.gpg123.nas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * frp客户端配置信息表
 * @TableName nas_frp_client
 */
@TableName(value ="nas_frp_client")
@Data
public class NasFrpClient {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 服务
     */
    @TableField(value = "frp_server")
    private String frpServer;

    /**
     * 协议类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * ip地址
     */
    @TableField(value = "local_ip")
    private String localIp;

    /**
     * 端口号
     */
    @TableField(value = "local_port")
    private Integer localPort;

    /**
     * 域名
     */
    @TableField(value = "custom_domains")
    private String customDomains;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 备注
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
