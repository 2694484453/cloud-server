package vip.gpg123.kubernetes.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName user_bind_cluster
 */
@TableName(value ="user_bind_cluster")
@Data
public class UserBindCluster {
    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     *
     */
    private String userId;

    /**
     *
     */
    private String clusterName;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private String updateBy;
}
