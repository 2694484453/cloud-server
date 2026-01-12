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
 * 用户词条
 * @TableName user_prompt
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="user_prompt")
@Data
public class UserPrompt extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "prompt_name")
    private String promptName;

    /**
     * 
     */
    @TableField(value = "prompt")
    private String prompt;

    /**
     * 
     */
    @TableField(value = "negative_prompt")
    private String negativePrompt;

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