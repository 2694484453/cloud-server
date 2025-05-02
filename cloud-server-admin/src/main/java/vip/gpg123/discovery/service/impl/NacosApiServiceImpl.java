package vip.gpg123.discovery.service.impl;

import vip.gpg123.discovery.service.NacosApiService;

import java.util.Map;

public abstract class NacosApiServiceImpl implements NacosApiService {

    /**
     * 创建ns
     *
     * @param params 参数
     * @return r
     */
    @Override
    public Boolean createNs(Map<String, String> params) {
        return null;
    }

    /**
     * 删除ns
     *
     * @param namespaceId id
     * @return r
     */
    @Override
    public Boolean deleteNs(String namespaceId) {
        return null;
    }
}
