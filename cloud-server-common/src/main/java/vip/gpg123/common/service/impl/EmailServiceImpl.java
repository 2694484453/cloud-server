package vip.gpg123.common.service.impl;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vip.gpg123.common.service.EmailService;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    /**
     * 不带附件邮件
     * @param subject 主题
     * @param content 内容
     * @param sendTo 定义可变参数 实现邮件发送多个邮箱
     */
    @SneakyThrows(Exception.class)
    public void sendSimpleMail(String subject, String content,String... sendTo) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(sendFrom);
        messageHelper.setTo(sendTo);
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        mailSender.send(message);
    }

    /**
     * 带附件邮件
     * @param subject 主题
     * @param content 内容
     *
     * @param filePath 附件路径
     * @param sendTo 定义可变参数 实现邮件发送多个邮箱
     */
    @SneakyThrows(Exception.class)
    public void sendAttachmentsMail(String subject, String content,String attachmentName, String filePath, String... sendTo){

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setFrom(sendFrom);
        messageHelper.setTo(sendTo);
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        //判断附件
        if(StrUtil.isNotBlank(filePath)){
            FileSystemResource file = new FileSystemResource(new File(filePath));
            //没有传递附件名称则默认使用文件名
            attachmentName = StrUtil.isNotBlank(filePath)?attachmentName:filePath.substring(filePath.lastIndexOf(File.separator));
            messageHelper.addAttachment(attachmentName, file);
        }
        mailSender.send(message);
    }
}
