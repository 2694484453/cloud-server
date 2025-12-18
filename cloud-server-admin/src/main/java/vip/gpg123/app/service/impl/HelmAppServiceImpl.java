package vip.gpg123.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.app.domain.HelmApp;
import vip.gpg123.app.service.HelmAppService;
import vip.gpg123.app.mapper.MineAppMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.framework.message.MessageProducer;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.mapper.KubernetesClusterMapper;

import java.io.File;
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
    private KubernetesClusterMapper kubernetesClusterMapper;

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
                KubernetesCluster kubernetesCluster = kubernetesClusterMapper.selectOne(new LambdaQueryWrapper<KubernetesCluster>().eq(KubernetesCluster::getContextName, entity.getKubeContext()));
                if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
                    File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
                    File valuesFile = K8sUtil.exportValuesToTempFile(entity.getChartValues());
                    String result = HelmUtils.install(entity.getReleaseName(), entity.getNameSpace(), entity.getChartUrl(), valuesFile.getAbsolutePath(), entity.getKubeContext(), file.getAbsolutePath());
                    entity.setResult(result);
                } else {
                    entity.setResult("找不到集群：" + entity.getKubeContext());
                }
                entity.setUpdateTime(DateUtil.date());
                entity.setStatus("ok");
                baseMapper.updateById(entity);
                // 发送消息
                producer.sendEmail("安装应用", modelName, res, sysUser.getEmail(), true);
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
                    KubernetesCluster kubernetesCluster = kubernetesClusterMapper.selectOne(new LambdaQueryWrapper<KubernetesCluster>().eq(KubernetesCluster::getContextName, entity.getKubeContext()));
                    if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
                        File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
                        File valuesFile = K8sUtil.exportValuesToTempFile(entity.getChartValues());
                        String result = HelmUtils.upgrade(entity.getReleaseName(), entity.getNameSpace(), entity.getChartUrl(), valuesFile.getAbsolutePath(), entity.getKubeContext(), file.getAbsolutePath());
                        entity.setResult(result);
                    } else {
                        entity.setResult("找不到集群：" + helmApp.getKubeContext());
                    }
                }
                entity.setStatus("ok");
                entity.setUpdateTime(DateUtil.date());
                baseMapper.updateById(entity);
                // 发送消息
                producer.sendEmail("更新应用", modelName, res, sysUser.getEmail(), true);
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
        KubernetesCluster kubernetesCluster = kubernetesClusterMapper.selectOne(new LambdaQueryWrapper<KubernetesCluster>().eq(KubernetesCluster::getContextName, entity.getKubeContext()));
        if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
            File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
            String result = HelmUtils.uninstall(entity.getNameSpace(), entity.getReleaseName(), entity.getKubeContext(), file.getAbsolutePath());
            entity.setResult(result);
        } else {
            entity.setResult("找不到集群：" + entity.getKubeContext());
        }
        entity.setUpdateTime(DateUtil.date());
        baseMapper.updateById(entity);
        boolean res = super.removeById(id);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                // 发送消息
                producer.sendEmail("卸载应用", modelName, res, sysUser.getEmail(), true);
            }
        });
        return res;
    }

}




