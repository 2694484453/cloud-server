package vip.gpg123.amqp.consumer;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.NoticeBody;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;

/**
 * 行为消息处理
 */
@Component
@Slf4j
public class NoticeConsumer {

    @Autowired
    private ISysNoticeService sysNoticeService;

    /**
     * 处理
     *
     * @param noticeBody s
     */
    @RabbitListener(queues = "cloud-server-message-notice")
    public void receive(NoticeBody noticeBody) {
        SysNotice sysNotice = new SysNotice();
        sysNotice.setCreateBy("system");
        sysNotice.setCreateTime(DateUtil.date());
        BeanUtils.copyProperties(noticeBody, sysNotice);
        // 执行保存
        sysNoticeService.save(sysNotice);
        log.info("{}:站内通知发送完成", sysNotice);
    }

}
