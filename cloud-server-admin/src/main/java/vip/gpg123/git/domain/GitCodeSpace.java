package vip.gpg123.git.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.gpg123.common.core.domain.BaseEntity;

/**
 * 代码空间
 * @TableName git_code_space
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="git_code_space")
@Data
public class GitCodeSpace extends BaseEntity implements Serializable {

    /**
     * 
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 
     */
    @TableField(value = "space_name")
    private String spaceName;

    /**
     * 
     */
    @TableField(value = "space_size")
    private String spaceSize;

    /**
     * 
     */
    @TableField(value = "space_status")
    private String spaceStatus;

    /**
     *
     */
    @TableField(value = "space_path")
    private String spacePath;

    /**
     *
     */
    @TableField(value = "space_url")
    private String spaceUrl;

    /**
     * 
     */
    @TableField(value = "repo_id")
    private Integer repoId;

    /**
     * 
     */
    @TableField(value = "type")
    private String type;

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