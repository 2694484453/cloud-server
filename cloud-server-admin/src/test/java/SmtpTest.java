import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.mchange.net.SmtpUtils;
import org.junit.Test;

import javax.mail.Session;
import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author gaopuguang
 * @date 2024/11/30 2:01
 **/
public class SmtpTest {

    private static final String SMTP_HOST = "smtp.qcloudmail.com";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_AUTH_USERNAME = "email@gpg123.vip";
    private static final String SMTP_AUTH_PASSWORD = "Gaopuguang@2023%D";
    private static final String SMTP_FROM = "email@gpg123.vip";
    private static final String SMTP_TO = "2694484453@qq.com";

    @Test
    public void t1() {
        try {
            SmtpUtils.sendMail(InetAddress.getByName(SMTP_HOST), SMTP_PORT, SMTP_FROM, new String[]{"2694484453@qq.com"}, new Properties(), new byte[]{'1', '2'});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void t2() {
        MailAccount mailAccount = new MailAccount()
                .setAuth(true)
                .setCharset(StandardCharsets.UTF_8)
                .setFrom(SMTP_FROM)
                .setUser(SMTP_AUTH_USERNAME)
                .setPass(SMTP_AUTH_PASSWORD)
                .setHost(SMTP_HOST)
                .setPort(SMTP_PORT);
        Session session = MailUtil.getSession(mailAccount, true);
        String res = MailUtil.sendHtml(SMTP_TO, "测试", "邮件测试内容", new File("D:\\AccessKey.csv"));
        System.out.println(res);
    }
}
