package vip.gpg123.kubernetes.service.impl;

import cn.hutool.core.io.FileUtil;
import io.fabric8.kubernetes.api.model.Config;
import io.fabric8.kubernetes.api.model.NamedCluster;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import org.springframework.stereotype.Service;
import vip.gpg123.kubernetes.service.ClusterService;
import vip.gpg123.framework.config.KubernetesClientConfig;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ClusterServiceImpl implements ClusterService {


    /**
     * 全部集群
     *
     * @return r
     */
    @Override
    public List<NamedCluster> list() {
        List<NamedCluster> clusterList;
        try {
            // 配置文件信息
            File file = FileUtil.file(KubernetesClientConfig.defaultConfigPath());
            Config config = KubeConfigUtils.parseConfig(file);
            clusterList = config.getClusters();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return clusterList;
    }

    /**
     * 某个集群
     *
     * @return r
     */
    @Override
    public NamedCluster one(String name) {
        AtomicReference<NamedCluster> namedCluster = null;
        try {
            // 读取
            List<NamedCluster> clusterList = list();
            clusterList.forEach(e -> {
                if (name.equals(e.getName())) {
                    namedCluster.set(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return namedCluster.get();
    }
}
