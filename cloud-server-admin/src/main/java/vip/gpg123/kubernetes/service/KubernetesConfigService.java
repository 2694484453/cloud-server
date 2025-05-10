package vip.gpg123.kubernetes.service;

import java.util.Map;

public interface KubernetesConfigService {

    /**
     * 添加
     * @param config 配置
     * @return r
     */
    boolean addConfig(Map<String, Object> config);

    /**
     * 更新
     * @param config 配置
     * @return r
     */
    boolean updateConfig(Map<String, Object> config);

    /**
     * 删除配置
     * @param name 名称
     * @return r
     */
    boolean deleteConfig(String name);
}
