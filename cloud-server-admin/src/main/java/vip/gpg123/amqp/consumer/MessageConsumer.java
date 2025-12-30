package vip.gpg123.amqp.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.service.EmailService;
import vip.gpg123.system.domain.SysActionNotice;
import vip.gpg123.system.service.SysActionNoticeService;


/**
 * 邮件消息处理
 */
@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    public static final String email = "cloud-server-email";

    public static final String notice = "cloud-server-notice";

    /**
     * 当消费者从队列取出消息时的回调方法
     *
     * @param emailBody 消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = email, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = email), // 声明直接交换器
            key = "email" // 定义路由键
    ))
    public void email(EmailBody emailBody) {
        // 接收人
        String[] tos = emailBody.getTos();
        String to = ArrayUtil.join(tos, ",");
        // 执行发送
        emailService.sendSimpleMail(setTitle(emailBody), setContent(emailBody), to);
        System.out.println("邮件发送完成");
    }

    /**
     * 处理
     *
     * @param emailBody e
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = notice, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = notice), // 声明直接交换器
            key = "notice" // 定义路由键
    ))
    public void notice(EmailBody emailBody) {
        SysActionNotice sysNotice = new SysActionNotice();
        sysNotice.setCreateBy(emailBody.getUserId());
        sysNotice.setCreateTime(DateUtil.date());
        sysNotice.setTitle(setTitle(emailBody));
        sysNotice.setContent(setContent(emailBody));
        sysNotice.setToUser(emailBody.getUserName());
        sysNotice.setToAddress(StrUtil.join(",", (Object) emailBody.getTos()));
        // 执行保存
        sysActionNoticeService.save(sysNotice);
        log.info("{}:站内通知发送完成", sysNotice);
    }

    public static String setTitle(EmailBody emailBody) {
        return "cloud-server云服务平台：<" + emailBody.getAction() + ">操作" + (emailBody.getResult() ? "成功" : "失败") + "通知";
    }

    public static String setContent(EmailBody emailBody) {
        return "尊敬的用户" + emailBody.getUserName() + ":您刚刚对" + emailBody.getModelName() + "进行了" + emailBody.getAction() + "操作，此邮件由系统发出，请勿回复！！";
    }
}