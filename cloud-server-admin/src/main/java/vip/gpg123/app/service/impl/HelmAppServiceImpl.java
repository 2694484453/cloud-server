package vip.gpg123.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.domain.HelmEntity;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.app.mapper.MineAppMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.producer.MessageProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【helm_app】的数据库操作Service实现
 * @createDate 2025-04-27 23:35:55
 */
@Service
public class HelmAppServiceImpl extends ServiceImpl<MineAppMapper, HelmApp> implements HelmAppService {

    @Autowired
    private MessageProducer producer;

    @Autowired
    private HelmApi helmApi;

    private static final String modelName = "应用管理";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(HelmApp entity) {
        boolean res = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                HelmEntity helmEntity = new HelmEntity();
                BeanUtils.copyProperties(entity, helmEntity);
                String installRes = helmApi.install(helmEntity);
                entity.setUpdateTime(DateUtil.date());
                entity.setStatus("ok");
                entity.setResult(installRes);
                baseMapper.updateById(entity);
                // 发送消息
                producer.sendEmail("安装应用", modelName, res, sysUser, true);
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
    public boolean updateById(HelmApp entity) {
        HelmApp helmApp = this.getById(entity.getId());
        boolean res = super.updateById(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                if (!entity.getChartValues().equals(helmApp.getChartValues())) {
                    entity.setStatus("updating");
                    baseMapper.updateById(entity);
                    HelmEntity helmEntity = new HelmEntity();
                    helmEntity.setChartUrl(entity.getChartUrl());
                    helmEntity.setChartValues(entity.getChartValues());
                    helmEntity.setKubeContext(entity.getKubeContext());
                    helmEntity.setNameSpace(entity.getNameSpace());
                    String result = helmApi.upgrade(helmEntity);
                    entity.setUpdateTime(DateUtil.date());
                    entity.setStatus("ok");
                    entity.setResult(result);
                }
                entity.setStatus("ok");
                entity.setUpdateTime(DateUtil.date());
                baseMapper.updateById(entity);
                // 发送消息
                producer.sendEmail("更新应用", modelName, res, sysUser, true);
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
        HelmApp entity = this.getById(id);
        HelmEntity helmEntity = new HelmEntity();
        BeanUtils.copyProperties(entity, helmEntity);
        String result = helmApi.uninstall(helmEntity);
        entity.setResult(result);
        entity.setUpdateTime(DateUtil.date());
        baseMapper.updateById(entity);
        boolean res = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 发送消息
                producer.sendEmail("卸载应用", modelName, res, sysUser, true);
            }
        });
        return res;
    }

}




