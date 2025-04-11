package vip.gpg123.framework.manager.factory;

import cn.hutool.extra.mail.MailUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.common.constant.Constants;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.LogUtils;
import vip.gpg123.common.utils.ServletUtils;
import vip.gpg123.common.utils.StringUtils;
import vip.gpg123.common.utils.ip.AddressUtils;
import vip.gpg123.common.utils.ip.IpUtils;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.framework.config.EmailConfig;
import vip.gpg123.system.domain.SysLogininfor;
import vip.gpg123.system.domain.SysOperLog;
import vip.gpg123.system.service.ISysLogininforService;
import vip.gpg123.system.service.ISysOperLogService;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author gpg123
 */
public class AsyncFactory {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
                // 发送通知邮件

            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 发送注册通知
     *
     * @param sysUser 用户
     * @return r
     */
    public static TimerTask sendRegisterEmail(final SysUser sysUser) {
        return new TimerTask() {
            @Override
            public void run() {
                MailUtil.send(EmailConfig.createMailAccount(), sysUser.getEmail(), "新用户注册通知邮件", "尊敬的" + sysUser.getEmail() + "用户：感谢注册cloud-server平台！", false);
            }
        };
    }
}
