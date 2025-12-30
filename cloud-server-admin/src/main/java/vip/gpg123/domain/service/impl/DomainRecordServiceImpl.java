package vip.gpg123.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.domain.domain.CloudDomain;
import vip.gpg123.domain.service.CloudDomainService;
import vip.gpg123.domain.mapper.CloudDomainMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.producer.MessageProducer;
import vip.gpg123.framework.producer.UmamiProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【app_domain(应用域名信息)】的数据库操作Service实现
 * @createDate 2025-06-06 01:16:02
 */
@Service
public class DomainRecordServiceImpl extends ServiceImpl<CloudDomainMapper, CloudDomain> implements CloudDomainService {

    @Autowired
    private UmamiProducer umamiProducer;

    @Autowired
    private MessageProducer messageProducer;

    public static final String modelName = "域名管理";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(CloudDomain entity) {
        boolean res = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 创建
                umamiProducer.createWebsite(entity.getDomain(), entity.getId(), entity.getCreateBy());
                // 发送
                messageProducer.sendEmail("新增", modelName, res, sysUser.getEmail(), true);
            }
        });
        return res;
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean res = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 删除
                umamiProducer.deleteWebsite(String.valueOf(id));
                // 发送
                messageProducer.sendEmail("删除", modelName, res, sysUser.getEmail(), true);
            }
        });
        return res;
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    public boolean updateById(CloudDomain entity) {
        boolean res = super.updateById(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                umamiProducer.updateWebsite(entity.getDomain(), entity.getId(), entity.getCreateBy());
                //
                messageProducer.sendEmail("修改", modelName, res, sysUser.getEmail(), true);
            }
        });
        return res;
    }
}




