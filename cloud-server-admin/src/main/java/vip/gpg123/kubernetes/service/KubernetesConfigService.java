package vip.gpg123.kubernetes.service;

import java.util.Map;

public interface KubernetesConfigService {

    /**
     * 添加
     * @param config 配置
     * @return r
     */
    boolean addConfig(Map<String, Object> config);
}
