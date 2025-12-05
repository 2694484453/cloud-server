package vip.gpg123.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.constant.CacheConstants;
import vip.gpg123.common.constant.UserConstants;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.core.domain.model.RegisterBody;
import vip.gpg123.common.core.redis.RedisCache;
import vip.gpg123.common.exception.user.CaptchaException;
import vip.gpg123.common.exception.user.CaptchaExpireException;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.system.producer.MessageProducer;
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

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username, password = registerBody.getPassword(), email = registerBody.getEmail();

        // 判断邮箱
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
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else {
            //
            sysUser.setNickName("请更改昵称");
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
                throw new RuntimeException(msg);
            } else {
                msg = "注册成功,可以进行登录了";
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        // 发送邮件
                        EmailBody emailBody = new EmailBody();
                        emailBody.setTos(new String[]{email});
                        emailBody.setTitle("云服务平台，欢迎注册！");
                        emailBody.setContent("您已完成平台注册请登陆：<a href='https://cloud-web.gpg123.vip'>点击登陆</a>");
                        emailBody.setHtml(true);
                        messageProducer.sendEmail(emailBody);
                    }
                });
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
