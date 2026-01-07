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
 * @TableName cloud_wallpaper
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="static_wallpaper")
@Data
public class StaticWallpaper extends BaseEntity implements Serializable {
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
     *
     */
    @TableField(value = "resolution")
    private String resolution;

    /**
     *
     */
    @TableField(value = "width")
    private Integer width;

    /**
     *
     */
    @TableField(value = "height")
    private Integer height;

    /**
     * 
     */
    @TableField(value = "tags")
    private String tags;

    /**
     *
     */
    @TableField(value = "dir_name")
    private String dirName;

    /**
     *
     */
    @TableField(value = "dir_path")
    private String dirPath;

    /**
     *
     */
    @TableField(value = "parent_name")
    private String parentName;

    /**
     *
     */
    @TableField(value = "description")
    private String description;

    /**
     *
     */
    @TableField(value = "url")
    private String url;

    /**
     * 
     */
    @TableField(value = "size")
    private String size;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}