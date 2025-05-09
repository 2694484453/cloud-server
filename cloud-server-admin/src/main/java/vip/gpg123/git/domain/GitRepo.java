package vip.gpg123.git.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName git
 */
@TableName(value ="git_repo")
@Data
public class GitRepo implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 归属
     */
    private String human_name;

    /**
     * 代号
     */
    private String code;

    /**
     * 类型
     */
    private String type;

    /**
     * 主页
     */
    private String homeUrl;

    /**
     * http地址
     */
    private String htmlUrl;

    /**
     * 仓库地址
     */
    private String gitUrl;

    /**
     * ssh地址
     */
    private String sshUrl;

    /**
     * 语言
     */
    private String language;

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
