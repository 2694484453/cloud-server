package vip.gpg123.git.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.amqp.producer.GitProducer;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.git.domain.GitCodeSpace;
import vip.gpg123.git.service.GitCodeSpaceService;
import vip.gpg123.git.mapper.GitCodeSpaceMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.message.MessageProducer;

import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【git_code_space(代码空间)】的数据库操作Service实现
 * @createDate 2025-11-30 02:17:54
 */
@Service
public class GitCodeSpaceServiceImpl extends ServiceImpl<GitCodeSpaceMapper, GitCodeSpace> implements GitCodeSpaceService {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private GitProducer gitProducer;

    private static final String modeName = "git仓库管理";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(GitCodeSpace entity) {
        boolean result = super.save(entity);
        SysUser user = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 发送邮件
                EmailBody emailBody = new EmailBody();
                emailBody.setAction("创建");
                emailBody.setName(entity.getSpaceName());
                emailBody.setResult(result);
                emailBody.setTos(new String[]{user.getEmail()});
                messageProducer.sendEmail("创建", modeName, result, user.getEmail(), true);
                //
                gitProducer.gitClone(entity);
            }
        });
        return result;
    }
}




