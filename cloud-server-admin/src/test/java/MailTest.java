import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vip.gpg123.CloudServerApplication;
import vip.gpg123.system.service.EmailService;

import javax.mail.Session;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author gaopuguang
 * @date 2025/2/22 0:02
 **/
@SpringBootTest(classes = CloudServerApplication.class)
@RunWith(SpringRunner.class)
public class MailTest {

    @Autowired
    private EmailService emailService;

    private static final String SMTP_HOST = "smtpdm.aliyun.com";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_AUTH_USERNAME = "admin@gpg123.vip";
    private static final String SMTP_AUTH_PASSWORD = "Gaopuguang2025Y";
    private static final String SMTP_FROM = "admin@gpg123.vip";
    private static final String SMTP_TO = "2694484453@qq.com";

    public MailAccount createMailAccount() {
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
        MailAccount account = createMailAccount();
        Session session = MailUtil.getSession(account, true);
        //session.setPasswordAuthentication(new URLName(), new PasswordAuthentication(account.getUser(), account.getPass()));
        MailUtil.send(createMailAccount(), "2694484453@qq.com", "the test title!", "for test", false);
    }

    @Test
    public void t2() {
        emailService.sendSimpleMail("测试", "test", "2694484453@qq.com");
    }



    @Test
    public void t3() {
        try {
            //SmtpUtils.sendMail(InetAddress.getByName(SMTP_HOST), SMTP_PORT, SMTP_FROM, new String[]{"2694484453@qq.com"}, new Properties(), new byte[]{'1', '2'});
            MailAccount mailAccount = createMailAccount();
            MailUtil.send(mailAccount, SMTP_TO, "11", "222", false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void t4() {
        MailAccount mailAccount = createMailAccount();
        Session session = MailUtil.getSession(mailAccount, true);
        String res = MailUtil.sendHtml(SMTP_TO, "测试", "邮件测试内容", new File("D:\\AccessKey.csv"));
        System.out.println(res);
    }

}
