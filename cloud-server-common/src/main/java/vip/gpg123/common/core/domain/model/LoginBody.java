package vip.gpg123.common.core.domain.model;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author gpg123
 */
@Data
public class LoginBody
{
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 集群
     */
    private String cluster;
}
