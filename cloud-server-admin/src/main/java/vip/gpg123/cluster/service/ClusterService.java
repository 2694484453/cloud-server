package vip.gpg123.cluster.service;

import io.fabric8.kubernetes.api.model.NamedCluster;

import java.util.List;

/**
 * 集群配置文件
 */
public interface ClusterService {

    /**
     * 全部集群
     * @return r
     */
    List<NamedCluster> list();

    /**
     * 某个集群
     * @return r
     */
    NamedCluster one(String name);
}
