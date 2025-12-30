package vip.gpg123.kubernetes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import vip.gpg123.common.core.domain.entity.SysUser;
import vip.gpg123.common.core.domain.model.LoginUser;
import vip.gpg123.common.utils.SecurityUtils;
import vip.gpg123.framework.manager.AsyncManager;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.service.KubernetesClusterService;
import vip.gpg123.kubernetes.mapper.KubernetesClusterMapper;
import org.springframework.stereotype.Service;
import vip.gpg123.framework.producer.MessageProducer;

import java.io.Serializable;
import java.util.TimerTask;

/**
 * @author Administrator
 * @description 针对表【kubernetes_server(k8s服务主机信息表)】的数据库操作Service实现
 * @createDate 2025-05-10 16:41:24
 */
@Service
public class KubernetesClusterServiceImpl extends ServiceImpl<KubernetesClusterMapper, KubernetesCluster> implements KubernetesClusterService {

    @Autowired
    private MessageProducer messageProducer;

    private static final String modeName = "边缘云集群";

    private static final String actionName = "添加集群";

    @Autowired
    private KubernetesClusterMapper kubernetesClusterMapper;

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(KubernetesCluster entity) {
        boolean result = super.save(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(actionName, modeName, result, sysUser.getUserName(), sysUser.getEmail(), true);
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
                messageProducer.sendEmail(actionName, modeName, result, sysUser.getUserName(), sysUser.getEmail(), true);
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
    public boolean updateById(KubernetesCluster entity) {
        boolean result = super.updateById(entity);
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                messageProducer.sendEmail(actionName, modeName, result, sysUser.getUserName(), sysUser.getEmail(), true);
            }
        });
        return result;
    }
}




