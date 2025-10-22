package vip.gpg123.tools.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 工具
 * @TableName web_tools
 */
@TableName(value ="web_tools")
@Data
public class WebTools implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "index")
    private Integer index;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 图
     */
    @TableField(value = "banner")
    private String banner;

    /**
     * 类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 启用
     */
    @TableField(value = "is_setup")
    private Integer isSetup;

    /**
     * 创建
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}