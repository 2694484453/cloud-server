package vip.gpg123.ai.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName ai_model
 */
@TableName(value ="ai_model")
@Data
public class AiModel implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 
     */
    @TableField(value = "description")
    private String description;

    /**
     * 
     */
    @TableField(value = "type")
    private String type;

    /**
     * 
     */
    @TableField(value = "status")
    private String status;

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