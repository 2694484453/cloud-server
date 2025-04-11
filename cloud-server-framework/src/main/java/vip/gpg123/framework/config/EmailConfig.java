package vip.gpg123.framework.config;

import cn.hutool.extra.mail.MailAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件配置
 *
 * @author gaopuguang
 * @date 2025/2/21 23:49
 **/
@Configuration
public class EmailConfig {

    /**
     * 账号bean
     *
     * @return r
     */
    @Bean
    public MailAccount mailAccount() {
        return createMailAccount();
    }

    /**
     * 创建mail账号
     * @return r
     */
    public static MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setPort(25);
        account.setHost("smtpdm.aliyun.com");
        account.setFrom("sysadmin@email.gpg123.vip");
        account.setUser("sysadmin@email.gpg123.vip");
        account.setPass("GAOpuguang2025");
        account.setSplitlongparameters(false);
        account.setTimeout(1000);
        account.setAuth(true);
        return account;
    }
}
