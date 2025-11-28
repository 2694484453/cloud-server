import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.junit.Test;

import javax.mail.Session;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2024/11/30 2:01
 **/
public class SmtpTest {

    private static final String SMTP_HOST = "smtpdm.aliyun.com";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_AUTH_USERNAME = "admin@gpg123.vip";
    private static final String SMTP_AUTH_PASSWORD = "Gaopuguang2025Y";
    private static final String SMTP_FROM = "admin@gpg123.vip";
    private static final String SMTP_TO = "2694484453@qq.com";

    public MailAccount getMailAccount() {
        return new MailAccount()
                .setAuth(true)
                .setCharset(StandardCharsets.UTF_8)
                .setSslEnable(true)
                .setFrom(SMTP_FROM)
                .setUser(SMTP_AUTH_USERNAME)
                .setPass(SMTP_AUTH_PASSWORD)
                .setHost(SMTP_HOST)
                .setPort(SMTP_PORT);
    }

    @Test
    public void t1() {
        try {
            //SmtpUtils.sendMail(InetAddress.getByName(SMTP_HOST), SMTP_PORT, SMTP_FROM, new String[]{"2694484453@qq.com"}, new Properties(), new byte[]{'1', '2'});
            MailAccount mailAccount = getMailAccount();
            MailUtil.send(mailAccount, SMTP_TO, "11", "222", false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void t2() {
        MailAccount mailAccount = getMailAccount();
        Session session = MailUtil.getSession(mailAccount, true);
        String res = MailUtil.sendHtml(SMTP_TO, "测试", "邮件测试内容", new File("D:\\AccessKey.csv"));
        System.out.println(res);
    }
}
