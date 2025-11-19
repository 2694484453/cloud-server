package vip.gpg123.helm.service.impl;


import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.helm.service.HelmApiService;

/**
 * helm抽象类
 */
public abstract class HelmApiServiceImpl implements HelmApiService {

    /**
     * 安装
     *
     * @param namespace   命名空间
     * @param chartUrl    chartUrl
     * @param kubeContext kubeContext
     * @return r
     */
    @Override
    public String install(String releaseName, String namespace, String chartUrl, String values, String kubeContext) {
        return HelmUtils.install(releaseName, namespace, chartUrl, values, kubeContext);
    }

    /**
     * 卸载
     *
     * @param namespace   命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     * @return r
     */
    @Override
    public String uninstall(String namespace, String releaseName, String kubeContext) {
        return HelmUtils.uninstall(namespace, releaseName, kubeContext);
    }
}
