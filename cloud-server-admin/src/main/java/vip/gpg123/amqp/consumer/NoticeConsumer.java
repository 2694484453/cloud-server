package vip.gpg123.amqp.consumer;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.gpg123.common.core.domain.model.NoticeBody;
import vip.gpg123.system.domain.SysActionNotice;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.system.service.SysActionNoticeService;

/**
 * 行为消息处理
 */
@Component
@Slf4j
public class NoticeConsumer {

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    /**
     * 处理
     *
     * @param noticeBody s
     */
    @RabbitListener(queues = "cloud-server-notice")
    public void receive(NoticeBody noticeBody) {
        SysActionNotice sysNotice = new SysActionNotice();
        sysNotice.setCreateBy("system");
        sysNotice.setCreateTime(DateUtil.date());
        BeanUtils.copyProperties(noticeBody, sysNotice);
        // 执行保存
        sysActionNoticeService.save(sysNotice);
        log.info("{}:站内通知发送完成", sysNotice);
    }

}
