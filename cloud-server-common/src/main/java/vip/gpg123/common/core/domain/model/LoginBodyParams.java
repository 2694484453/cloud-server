package vip.gpg123.common.core.domain.model;

import lombok.Data;
import lombok.Getter;

/**
 * 用户登录对象
 *
 * @author gpg123
 */
@Getter
@Data
public class LoginBodyParams
{
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String passWord;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;
}
