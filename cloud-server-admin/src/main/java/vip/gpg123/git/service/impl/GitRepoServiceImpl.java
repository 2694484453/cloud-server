package vip.gpg123.git.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.domain.model.LoginUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.producer.MessageProducer;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.service.GitRepoService;
import vip.gpg123.git.mapper.GitRepoMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【git_repo】的数据库操作Service实现
 * @createDate 2025-11-20 00:02:45
 */
@Service
public class GitRepoServiceImpl extends ServiceImpl<GitRepoMapper, GitRepo> implements GitRepoService {

    @Autowired
    private MessageProducer messageProducer;

    private static final String modeName = "git仓库管理";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(GitRepo entity) {
        boolean result = super.save(entity);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(modeName, "添加", result, loginUser.getUser().getEmail(), true);
            }
        });
        return result;
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(GitRepo entity) {
        return super.updateById(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(modeName, "删除", result, loginUser.getUser().getEmail(), true);
            }
        });
        return result;
    }
}




