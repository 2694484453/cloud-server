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
@Api(tags = "【用户注册】注册管理")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.contains(msg, "成功") ? AjaxResult.success(msg) : AjaxResult.error(msg, null);
    }
}
