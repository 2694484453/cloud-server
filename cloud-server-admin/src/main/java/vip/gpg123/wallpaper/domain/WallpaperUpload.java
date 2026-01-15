package vip.gpg123.wallpaper.domain;

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
 * @TableName cloud_wallpaper_upload
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="wallpaper_upload")
@Data
public class WallpaperUpload extends BaseEntity implements Serializable {
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
     * 模型名称
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 
     */
    @TableField(value = "size")
    private String size;

    /**
     * 
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 
     */
    @TableField(value = "url")
    private String url;

    /**
     *
     */
    @TableField(value = "request_ip")
    private String requestIp;

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