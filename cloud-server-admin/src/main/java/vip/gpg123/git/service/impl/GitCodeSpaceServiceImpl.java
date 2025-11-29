package vip.gpg123.git.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.EmailBody;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.git.domain.GitCodeSpace;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.service.GitCodeSpaceService;
import vip.gpg123.git.mapper.GitCodeSpaceMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.git.service.GitRepoService;
import vip.gpg123.git.service.GitTokenService;
import vip.gpg123.system.consumer.NoticeConsumer;
import vip.gpg123.system.producer.MessageProducer;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【git_code_space(代码空间)】的数据库操作Service实现
 * @createDate 2025-11-30 02:17:54
 */
@Service
public class GitCodeSpaceServiceImpl extends ServiceImpl<GitCodeSpaceMapper, GitCodeSpace> implements GitCodeSpaceService {

    @Value("${ide.path}")
    private String basePath;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitRepoService gitRepoService;


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
                messageProducer.sendEmail(emailBody, true);
                // 初始化克隆
                boolean isExist = FileUtil.exist(entity.getSpacePath());
                if (!isExist) {
                    GitRepo gitRepo = gitRepoService.getById(entity.getRepoId());
                    GitToken gitToken = gitTokenService.getOne(new LambdaQueryWrapper<GitToken>()
                            .eq(GitToken::getCreateBy, user.getUserId())
                            .eq(GitToken::getType, gitRepo.getType())
                    );
                    if (gitToken != null) {
                        String path = basePath + File.separator + user.getUserId() + File.separator + entity.getSpaceName();
                        List<String> list = StrUtil.split(path, "//");
                        String gitUrl = list.get(0) + "//" + gitToken.getUserName() + ":" + gitToken.getAccessToken() + "@" + list.get(1);
                        RuntimeUtil.execForStr(StandardCharsets.UTF_8, "cd", path, "&&", "git", "clone", gitUrl);
                    }
                }
            }
        });
        return result;
    }
}




