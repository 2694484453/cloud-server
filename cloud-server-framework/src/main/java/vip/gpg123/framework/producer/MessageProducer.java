package vip.gpg123.framework.producer;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.common.core.domain.model.NoticeBody;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.system.service.ISysUserService;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ISysUserService sysUserService;

    private static final String email = "cloud-server-email";

    private static final String notice = "cloud-server-notice";

    /**
     * 发送邮件
     *
     * @param emailBody e
     */
    public void sendEmail(EmailBody emailBody) {
        // 发送消息到交换机，并指定路由键
        rabbitTemplate.convertAndSend(email, emailBody);
        System.out.println("发送邮件: " + emailBody);
    }

    /**
     * 发送邮件
     */
    public void sendEmail(EmailBody emailBody, Boolean sendNotice) {
        // 发送消息到交换机，并指定路由键
        rabbitTemplate.convertAndSend(email, emailBody);
        if (sendNotice) {
            NoticeBody noticeBody = new NoticeBody();
            BeanUtils.copyProperties(emailBody, noticeBody);
            this.sendActionNotice(noticeBody);
        }
        System.out.println("发送邮件: " + emailBody);
    }

    /**
     * 发送邮件
     *
     * @param actionName 行为
     * @param modelName 模块名称
     * @param result 结果
     * @param to 发给
     * @param sendNotice 是否发送站内
     */
    public void sendEmail(String actionName, String modelName, boolean result,String userName, String to, Boolean sendNotice) {
        EmailBody emailBody = new EmailBody();
        emailBody.setAction(actionName);
        emailBody.setResult(result);
        emailBody.setUserName(userName);
        // 是否邮箱格式
        if (StrUtil.isNotBlank(to) && !Validator.isEmail(to)) {
            to = sysUserService.getById(to).getEmail();
        }
        if (StrUtil.isNotBlank(to) && Validator.isEmail(to) ) {
            emailBody.setTos(new String[]{to});
            emailBody.setModelName(modelName);
            // 发送消息到交换机，并指定路由键
            rabbitTemplate.convertAndSend(email, emailBody);
            if (sendNotice) {
                NoticeBody noticeBody = new NoticeBody();
                BeanUtils.copyProperties(emailBody, noticeBody);
                this.sendActionNotice(noticeBody);
            }
            System.out.println("发送邮件: " + emailBody);
        } else {
            System.out.println("不是邮箱地址: " + emailBody);
        }
    }

    /**
     * 发送站内消息
     *
     * @param noticeBody b
     */
    public void sendActionNotice(NoticeBody noticeBody) {
        rabbitTemplate.convertAndSend(notice, noticeBody);
        System.out.println("发送站内消息: " + noticeBody);
    }
}
