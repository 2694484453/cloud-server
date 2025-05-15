package vip.gpg123.nas.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.config.EmailConfig;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.nas.domain.NasFrpClient;
import vip.gpg123.nas.service.NasFrpClientService;
import vip.gpg123.nas.mapper.NasFrpClientMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.notice.domain.SysActionNotice;
import vip.gpg123.notice.service.SysActionNoticeService;

import java.io.Serializable;
import java.util.TimerTask;

import static vip.gpg123.common.utils.SecurityUtils.getLoginUser;
import static vip.gpg123.common.utils.SecurityUtils.getUsername;

/**
* @author Administrator
* @description 针对表【nas_frp_client(frp客户端配置信息表)】的数据库操作Service实现
* @createDate 2025-05-10 17:40:35
*/
@Service
public class NasFrpClientServiceImpl extends ServiceImpl<NasFrpClientMapper, NasFrpClient> implements NasFrpClientService{

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean isSuccess = super.removeById(id);
        String userName = getUsername();
        String userEmail = getLoginUser().getUser().getEmail();
        // 异步发送消息
        AsyncManager.me().execute(new TimerTask() {
            /**
             * The action to be performed by this timer task.
             */
            @Override
            public void run() {
                // 入库
                SysActionNotice sysActionNotice = new SysActionNotice();
                sysActionNotice.setTitle("删除frp客户端");
                sysActionNotice.setCreateBy(userName);
                sysActionNotice.setCreateTime(DateUtil.date());
                sysActionNotice.setContent(userName + "删除frp客户端，结果：" + (isSuccess ? "成功" : "失败"));
                sysActionNotice.setType("frpClient");
                sysActionNoticeService.save(sysActionNotice);
                // 邮件消息
                if (StrUtil.isNotBlank(userEmail)) {
                    Email email = new Email();
                    String[] tos = new String[]{};
                    tos = ArrayUtil.append(tos, userEmail);
                    email.setTos(tos);
                    email.setTitle("删除frp客户端" + (isSuccess ? "成功" : "失败") + "通知");
                    email.setContent(userName + "删除frp客户端，结果：" + (isSuccess ? "成功" : "失败"));
                    MailUtil.send(EmailConfig.createMailAccount(), userEmail, email.getTitle(), email.getContent(), false);
                }
            }

        });
        return isSuccess;
    }
}




