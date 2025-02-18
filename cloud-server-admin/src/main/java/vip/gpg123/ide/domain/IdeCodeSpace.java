package vip.gpg123.ide.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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
    private String tags;

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
