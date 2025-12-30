package vip.gpg123.amqp.consumer;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.service.EmailService;


/**
 * 邮件消息处理
 */
@Component
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    public static final String email = "cloud-server-email";

    /**
     * 当消费者从队列取出消息时的回调方法
     *
     * @param emailBody 消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = email, durable = "true"), // 创建持久化队列
            exchange = @Exchange(name = email), // 声明直接交换器
            key = email // 定义路由键
    ))
    public void receive(EmailBody emailBody) {
        // 接收人
        String[] tos = emailBody.getTos();
        String to = ArrayUtil.join(tos, ",");
        // 执行发送
        String title = "cloud-server云服务平台：<" + emailBody.getAction() + ">操作" + (emailBody.getResult() ? "成功" : "失败") + "通知";
        String content = "尊敬的用户" + emailBody.getUserName() + ":您刚刚对" + emailBody.getName() + "进行了" + emailBody.getAction() + "操作，此邮件由系统发出，请勿回复！！";
        emailService.sendSimpleMail(title, content, to);
        System.out.println("邮件发送完成");
    }
}