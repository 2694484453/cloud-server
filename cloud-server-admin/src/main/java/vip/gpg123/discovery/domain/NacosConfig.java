package vip.gpg123.discovery.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 服务发现配置信息表
 * @TableName nacos_config
 */
@TableName(value ="nacos_config")
@Data
public class NacosConfig implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "config_id")
    private String configId;

    /**
     * 命名空间id
     */
    @TableField(value = "name_space_id")
    private String nameSpaceId;

    /**
     * 分组名称
     */
    @TableField(value = "config_group")
    private String configGroup;

    /**
     * 名称
     */
    @TableField(value = "config_name")
    private String configName;

    /**
     * 文件扩展类型
     */
    @TableField(value = "config_file_type")
    private String configFileType;

    /**
     * 配置内容
     */
    @TableField(value = "config_content")
    private String configContent;

    /**
     * 状态
     */
    @TableField(value = "config_status")
    private String configStatus;

    /**
     * 描述
     */
    @TableField(value = "config_description")
    private String configDescription;

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