package vip.gpg123.wallpaper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName prompt_keyword
 */
@TableName(value ="prompt_keyword")
@Data
public class PromptKeyword implements Serializable {
    /**
     * 
     */
    @TableId(value = "keyword_id", type = IdType.AUTO)
    private Integer keywordId;

    /**
     * 
     */
    @TableField(value = "group_id")
    private Integer groupId;

    /**
     * 
     */
    @TableField(value = "keyword_name")
    private String keywordName;

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
    private Integer updateBy;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}