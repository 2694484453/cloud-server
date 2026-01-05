package vip.gpg123.system;

import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.controller.BaseController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.core.domain.model.RegisterBody;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.framework.config.EmailConfig;
import vip.gpg123.framework.producer.MessageProducer;
import vip.gpg123.framework.web.service.SysRegisterService;
import vip.gpg123.system.service.ISysConfigService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 注册验证
 *
 * @author gaopuguang
 */
@RestController
@Api(tags = "【用户注册】注册管理")
public class SysRegisterController extends BaseController {

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private EmailConfig.emailProperties emailProperties;

    /**
     * 注册
     *
     * @param user user
     * @return r
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        if (StringUtils.isEmpty(user.getCode())) {
            return error("验证码不能为空");
        }
        if (!verifyCode(user.getEmail(), user.getCode())) {
            return error("验证码错误");
        }
        String msg = registerService.register(user);
        return StringUtils.contains(msg, "成功") ? AjaxResult.success(msg) : AjaxResult.error(msg, null);
    }

    /**
     * 发送验证码
     *
     * @param map m
     */
    @PostMapping("/code")
    public AjaxResult code(@RequestBody Map<String, String> map) {
        try {
            String email = map.get("email");
            String site = map.getOrDefault("site", "云服务平台");
            String code = RandomUtil.randomNumbers(6);
            stringRedisTemplate.opsForValue().set(email, code, 10, TimeUnit.MINUTES);
            String content = emailProperties.getCodeContent();
            EmailBody emailBody = new EmailBody();
            emailBody.setTos(new String[]{email});
            emailBody.setHtml(true);
            emailBody.setTitle(site);
            // 使用字符串替换将占位符换成真实数据
            content = content.replace("123456", code);
            content = content.replace("你的网站/APP名称", site);
            emailBody.setContent(content);
            // 发送验证码
            messageProducer.sendEmail(emailBody);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success("发送成功");
    }

    public boolean verifyCode(String email, String inputCode) {
        String storedCode = stringRedisTemplate.opsForValue().get(email);
        if (storedCode != null && storedCode.equals(inputCode)) {
            // 验证成功，可以删除该键或保留至过期
            stringRedisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
