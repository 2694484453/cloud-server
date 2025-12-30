package vip.gpg123.devops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.constant.Constants;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.LoginUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.devops.domain.DevopsJob;
import vip.gpg123.devops.service.DevopsJobService;
import vip.gpg123.devops.mapper.DevopsJobMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.producer.MessageProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【devops_job(普通任务)】的数据库操作Service实现
 * @createDate 2025-05-08 01:57:47
 */
@Service
public class DevopsJobServiceImpl extends ServiceImpl<DevopsJobMapper, DevopsJob> implements DevopsJobService {

    @Autowired
    private MessageProducer messageProducer;

    private static final String modeName = "Devops流水线";


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(DevopsJob entity) {
        boolean result = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(Constants.ADD_ACTION, modeName, result, sysUser.getUserName(), sysUser.getEmail(), true);
            }
        });
        return result;
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(Constants.ADD_ACTION, modeName, result, sysUser.getUserName(), sysUser.getEmail(), true);
            }
        });
        return result;
    }
}




