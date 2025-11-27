package vip.gpg123.system.service;

public interface EmailService {

    /**
     * 不带附件邮件
     *
     * @param subject 主题
     * @param content 内容
     * @param sendTo  定义可变参数 实现邮件发送多个邮箱
     */
    public void sendSimpleMail(String subject, String content, String... sendTo);

    /**
     * 带附件邮件
     *
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件路径
     * @param sendTo   定义可变参数 实现邮件发送多个邮箱
     */
    public void sendAttachmentsMail(String subject, String content, String attachmentName, String filePath, String... sendTo);
}
