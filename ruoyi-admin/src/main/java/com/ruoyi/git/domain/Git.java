package com.ruoyi.git.domain;

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
@TableName(value ="git")
@Data
public class Git implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String code;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private String home;

    /**
     *
     */
    private String httpUrl;

    /**
     *
     */
    private String sshUrl;

    /**
     *
     */
    private Boolean hasJob;

    /**
     *
     */
    private String language;

    /**
     *
     */
    private Object jobTypes;

    /**
     *
     */
    private Integer jobCount;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String updateBy;

    /**
     *
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
