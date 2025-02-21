package vip.gpg123.framework.config;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

import javax.mail.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaopuguang
 * @date 2025/2/21 23:49
 **/
@Configuration
public class MailConfig {

    @Bean
    public MailAccount mailAccount() {
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
