import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.junit.Test;

import javax.mail.Session;

/**
 * @author gaopuguang
 * @date 2025/2/22 0:02
 **/
public class MailTest {

    @Test
    public void t1() {
        MailAccount account = createMailAccount();
        Session session = MailUtil.getSession(account, true);
        //session.setPasswordAuthentication(new URLName(), new PasswordAuthentication(account.getUser(), account.getPass()));
        MailUtil.send(createMailAccount(), "2694484453@qq.com", "the test title!", "for test",false);
    }


    public MailAccount createMailAccount() {
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
