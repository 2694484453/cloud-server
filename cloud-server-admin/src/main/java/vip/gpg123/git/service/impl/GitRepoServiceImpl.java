package vip.gpg123.git.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.domain.Email;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.git.domain.GitRepo;
import vip.gpg123.git.service.GitRepoService;
import vip.gpg123.git.mapper.GitRepoMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.notice.domain.SysActionNotice;
import vip.gpg123.notice.service.SysActionNoticeService;

import java.io.Serializable;
import java.util.Collection;
import java.util.TimerTask;

import static vip.gpg123.common.utils.SecurityUtils.getLoginUser;
import static vip.gpg123.common.utils.SecurityUtils.getUsername;


/**
 * @author Administrator
 * @description 针对表【git】的数据库操作Service实现
 * @createDate 2024-12-01 01:00:54
 */
@Service
public class GitRepoServiceImpl extends ServiceImpl<GitRepoMapper, GitRepo> implements GitRepoService {

    @Autowired
    private SysActionNoticeService sysActionNoticeService;

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        GitRepo entity = getById(id);
        boolean isSuccess = GitRepoService.super.removeById(id);
        String userName = getUsername();
        String userEmail = getLoginUser().getUser().getEmail();
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                String message = "删除";
                // 站内消息
                SysActionNotice sysActionNotice = new SysActionNotice();
                sysActionNotice.setTitle("Git仓库导入" + message + "通知");
                sysActionNotice.setCreateBy(userName);
                sysActionNotice.setCreateTime(DateUtil.date());
                sysActionNotice.setContent(userName + message + entity.getName() + "Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
                sysActionNotice.setType("gitRepo");
                sysActionNoticeService.save(sysActionNotice);
                // 邮件消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库导入" + message + "通知");
                email.setContent(userName + message + entity.getName() + "Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess;
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    public boolean saveOrUpdateBatch(Collection<GitRepo> entityList) {
        return GitRepoService.super.saveOrUpdateBatch(entityList);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Override
    public boolean saveOrUpdate(GitRepo entity) {
        // 判断是更新还是新增
        GitRepo search = getById(entity.getId());
        boolean isSuccess = super.saveOrUpdate(entity);
        String userName = getUsername();
        String userEmail = getLoginUser().getUser().getEmail();
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                String message = search == null ? "新增" : "更新";
                // 站内消息
                SysActionNotice sysActionNotice = new SysActionNotice();
                sysActionNotice.setTitle("Git仓库导入" + message + "通知");
                sysActionNotice.setCreateBy(userName);
                sysActionNotice.setCreateTime(DateUtil.date());
                sysActionNotice.setContent(userName + message + entity.getName() + " Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
                sysActionNotice.setType("gitRepo");
                sysActionNoticeService.save(sysActionNotice);
                // 邮件消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库导入" + message + "通知");
                email.setContent(userName + message + entity.getName() + " Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess;
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Override
    public boolean saveBatch(Collection<GitRepo> entityList) {
        return GitRepoService.super.saveBatch(entityList);
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(GitRepo entity) {
        boolean isSuccess = GitRepoService.super.save(entity);
        String userName = getUsername();
        String userEmail = getLoginUser().getUser().getEmail();
        // 异步消息
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                String message = "新增";
                // 站内消息
                SysActionNotice sysActionNotice = new SysActionNotice();
                sysActionNotice.setTitle("Git仓库导入" + message + "通知");
                sysActionNotice.setCreateBy(userName);
                sysActionNotice.setCreateTime(DateUtil.date());
                sysActionNotice.setContent(userName + message + entity.getName() + " Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
                sysActionNotice.setType("gitRepo");
                sysActionNoticeService.save(sysActionNotice);
                // 邮件消息
                Email email = new Email();
                String[] tos = new String[]{};
                tos = ArrayUtil.append(tos, userEmail);
                email.setTos(tos);
                email.setTitle("Git仓库导入" + message + "通知");
                email.setContent(userName + message + entity.getName() + " Git仓库，结果：" + (isSuccess ? "成功" : "失败"));
            }
        });
        return isSuccess;
    }
}




