package vip.gpg123.dashboard.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName dou_area_world
 */
@TableName(value ="dou_area_world")
@Data
public class DouAreaWorld implements Serializable {
    /**
     * 
     */
    @TableId(value = "area_id", type = IdType.AUTO)
    private Integer areaId;

    /**
     * 
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 
     */
    @TableField(value = "name")
    private String name;

    /**
     * 
     */
    @TableField(value = "iso_code_2")
    private String isoCode2;

    /**
     * 
     */
    @TableField(value = "iso_code_3")
    private String isoCode3;

    /**
     * 
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}