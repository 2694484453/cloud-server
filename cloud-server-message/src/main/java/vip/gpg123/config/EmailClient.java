package vip.gpg123.config;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailClient {

    /**
     * 邮箱配置
     * @return r
     */
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

    /**
     * 发送邮件-单个发送
     * @param to to
     * @param title t
     * @param content c
     * @return r
     */
    public String send(String to, String title, String content) {
        MailAccount mailAccount = this.mailAccount();
        return MailUtil.send(mailAccount,to, title, content, false);
    }

}
