package vip.gpg123.framework.web.service;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.constant.CacheConstants;
import vip.gpg123.common.constant.Constants;
import vip.gpg123.common.constant.UserConstants;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.RegisterBody;
import vip.gpg123.common.core.redis.RedisCache;
import vip.gpg123.common.exception.user.CaptchaException;
import vip.gpg123.common.exception.user.CaptchaExpireException;
import vip.gpg123.common.utils.MessageUtils;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.manager.factory.AsyncFactory;
import vip.gpg123.system.service.ISysConfigService;
import vip.gpg123.system.service.ISysUserService;

import java.util.TimerTask;

/**
 * 注册校验方法
 *
 * @author gpg123
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword(), phone = registerBody.getPhone(), email = registerBody.getEmail(), cluster = registerBody.getCluster();

        // 判断使用类型
        switch (registerBody.getType()) {
            // 账号登录
            case "username":
                if (StringUtils.isEmpty(username)) {
                    msg = "用户名不能为空";
                }
                break;

            // 邮箱登录
            case "email":
                if (StringUtils.isEmpty(email)) {
                    msg = "邮箱不能为空";
                }
                username = email;
                // 检查邮箱是否被使用
                int count = userService.count(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getEmail, email)
                );
                if (count >= 1) {
                    msg = "已存在相同邮箱";
                    throw new RuntimeException(msg);
                }
                break;

            // 手机号登录
            case "phone":
                if (StringUtils.isEmpty(phone)) {
                    msg = "手机号不能为空";
                }
                break;
            // 使用集群注册
            case "cluster":
                username = cluster;
                if (StringUtils.isEmpty(cluster)) {
                    msg = "集群域名/IP不能为空";
                    throw new RuntimeException(msg);
                }
                int clusterCount = userService.count(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUserName, cluster)
                );
                if (clusterCount >=1 ) {
                    msg = "已存在相同集群域名/IP，不允许再注册";
                    throw new RuntimeException(msg);
                }
                break;
        }

        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setEmail(registerBody.getEmail());
        sysUser.setPhonenumber(registerBody.getPhone());

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {

            //
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
                throw new RuntimeException(msg);
            } else {
                msg = "注册成功,可以进行登录";
                // 登录日志
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));

                // 发送邮件
                AsyncManager.me().execute(AsyncFactory.sendRegisterEmail(sysUser));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
