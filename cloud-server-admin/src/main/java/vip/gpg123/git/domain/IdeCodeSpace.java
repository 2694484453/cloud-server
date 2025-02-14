package vip.gpg123.git.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName ide_code_space
 */
@TableName(value ="ide_code_space")
@Data
public class IdeCodeSpace {
    /**
     *
     */
    @TableId
    private String name;

    /**
     *
     */
    private String gitId;

    /**
     *
     */
    private String gitHttp;

    /**
     *
     */
    private String workPath;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private String updateBy;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;
}
