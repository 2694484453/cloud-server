package vip.gpg123.prometheus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import vip.gpg123.prometheus.service.PrometheusTargetService;
import vip.gpg123.prometheus.mapper.PrometheusExporterMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.producer.MessageProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_exporter】的数据库操作Service实现
 * @createDate 2025-11-19 01:40:53
 */
@Service
public class PrometheusTargetServiceImpl extends ServiceImpl<PrometheusExporterMapper, PrometheusTarget> implements PrometheusTargetService {

    @Autowired
    private MessageProducer messageProducer;

    private static final String modelName = "Prometheus端点";

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(PrometheusTarget entity) {
        boolean result = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 发送邮件
                messageProducer.sendEmail("新增", modelName, result, sysUser, true);
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
    public boolean updateById(PrometheusTarget entity) {
        return super.updateById(entity);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    public boolean removeById(Serializable id) {
        boolean flag = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail("删除", modelName, flag, sysUser, true);
            }
        });
        return flag;
    }
}




