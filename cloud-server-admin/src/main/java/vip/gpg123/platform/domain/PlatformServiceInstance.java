package vip.gpg123.platform.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 平台服务实例配置信息表
 * @TableName platform_service_instance
 */
@TableName(value ="platform_service_instance")
@Data
public class PlatformServiceInstance implements Serializable {
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
     * 类型
     */
    @TableField(value = "type")
    private String type;

    /**
     * host地址
     */
    @TableField(value = "host")
    private String host;

    /**
     * 面板地址
     */
    @TableField(value = "dashboard_path")
    private String dashboard_path;

    /**
     * frame嵌入地址
     */
    @TableField(value = "frame_path")
    private String frame_path;

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
    private String create_by;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String update_by;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}