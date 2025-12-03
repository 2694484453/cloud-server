package vip.gpg123.system.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.common.core.domain.model.NoticeBody;
import vip.gpg123.common.core.domain.model.EmailBody;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String emailRoutingKey = "cloud-server-message-email";

    private static final String actionNoticeRoutingKey = "cloud-server-message-notice";

    /**
     * 发送邮件
     *
     * @param emailBody e
     */
    public void sendEmail(EmailBody emailBody) {
        // 发送消息到交换机，并指定路由键
        rabbitTemplate.convertAndSend(emailRoutingKey, emailBody);
        System.out.println("发送邮件: " + emailBody);
    }

    /**
     * 发送邮件
     */
    public void sendEmail(EmailBody emailBody, Boolean sendNotice) {
        // 发送消息到交换机，并指定路由键
        rabbitTemplate.convertAndSend(emailRoutingKey, emailBody);
        if (sendNotice) {
            NoticeBody noticeBody = new NoticeBody();
            BeanUtils.copyProperties(emailBody, noticeBody);
            this.sendActionNotice(noticeBody);
        }
        System.out.println("发送邮件: " + emailBody);
    }

    /**
     * 发送邮件
     */
    public void sendEmail(String actionName, String modelName, boolean result, String to, Boolean sendNotice) {
        EmailBody emailBody = new EmailBody();
        emailBody.setAction(actionName);
        emailBody.setResult(result);
        emailBody.setTos(new String[]{to});
        emailBody.setModelName(modelName);
        // 发送消息到交换机，并指定路由键
        rabbitTemplate.convertAndSend(emailRoutingKey, emailBody);
        if (sendNotice) {
            NoticeBody noticeBody = new NoticeBody();
            BeanUtils.copyProperties(emailBody, noticeBody);
            this.sendActionNotice(noticeBody);
        }
        System.out.println("发送邮件: " + emailBody);
    }

    /**
     * 发送站内消息
     *
     * @param noticeBody b
     */
    public void sendActionNotice(NoticeBody noticeBody) {
        rabbitTemplate.convertAndSend(actionNoticeRoutingKey, noticeBody);
        System.out.println("发送站内消息: " + noticeBody);
    }
}
