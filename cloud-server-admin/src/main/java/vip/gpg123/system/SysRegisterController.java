package vip.gpg123.system;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.RegisterBody;
import vip.gpg123.common.core.domain.model.RegisterUserByMail;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.web.service.SysRegisterService;
import vip.gpg123.system.service.ISysConfigService;
import vip.gpg123.system.service.ISysUserService;

import java.util.TimerTask;

/**
 * 注册验证
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/register")
@Api(tags = "【用户注册】注册管理")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private MailAccount mailAccount;

    @PostMapping("/registerNon")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    /**
     * 邮箱注册
     *
     * @param register r
     * @return r
     */
    @PostMapping("/byMail")
    public AjaxResult registerByMail(@RequestBody RegisterUserByMail register) {
        //
        SysUser user = new SysUser();
        user.setEmail(register.getEmail());
        boolean isEmailExist = sysUserService.checkEmailUnique(user);
        if (isEmailExist) {
            return AjaxResult.warn("此邮箱已被使用！");
        } else {
            user.setPassword(register.getPassword());
            user.setNickName(register.getEmail());
            user.setUserName(register.getEmail());
            int r = sysUserService.insertUser(user);
            // 发送邮件
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    MailUtil.send(mailAccount, register.getEmail(), "新用户注册通知邮件", "尊敬的" + register.getEmail() + "用户：感谢注册cloud-server平台！", false);
                }
            });
            return r > 0 ? AjaxResult.success("注册成功，请查询邮箱通知！") : AjaxResult.error("注册失败，请重新！");
        }
    }
}
