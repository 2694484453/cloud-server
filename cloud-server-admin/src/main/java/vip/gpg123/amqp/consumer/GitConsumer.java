package vip.gpg123.amqp.consumer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vip.gpg123.amqp.producer.GitProducer;
import vip.gpg123.git.domain.GitCodeSpace;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.domain.GitToken;
import vip.gpg123.git.service.GitRepoService;
import vip.gpg123.git.service.GitTokenService;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
public class GitConsumer {

    @Autowired
    private GitTokenService gitTokenService;

    @Autowired
    private GitRepoService gitRepoService;

    @Value("${ide.path}")
    private String basePath;

    @Value("${ide.url}")
    private String url;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = GitProducer.gitQueue, durable = "true"),
            exchange = @Exchange(name = GitProducer.gitExchange),
            key = "gitClone"
    ))
    public void gitClone(GitCodeSpace entity) {
        // 初始化克隆
        boolean isExist = FileUtil.exist(entity.getSpacePath());
        if (!isExist) {
            GitRepo gitRepo = gitRepoService.getById(entity.getRepoId());
            GitToken gitToken = gitTokenService.getOne(new LambdaQueryWrapper<GitToken>()
                    .eq(GitToken::getCreateBy, entity.getCreateBy())
                    .eq(GitToken::getType, gitRepo.getType())
            );
            if (gitToken != null) {
                String path = basePath + File.separator + entity.getCreateBy();
                RuntimeUtil.execForStr(StandardCharsets.UTF_8, "cd", path, "&&", "git", "clone", entity.getRepoUrl());
            }
        }
    }
}
