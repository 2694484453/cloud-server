package vip.gpg123.helm.service.impl;


import org.springframework.stereotype.Service;
import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.helm.service.HelmApiService;

@Service
public abstract class HelmApiServiceImpl implements HelmApiService {

    /**
     * 安装
     *
     * @param namespace   命名空间
     * @param repoName    仓库名称
     * @param chartName   chart名称
     * @param version     版本
     * @param kubeContext kubeContext
     */
    @Override
    public void install(String namespace, String repoName, String chartName, String version, String kubeContext) {
        HelmUtils.install(namespace, repoName, chartName, version, kubeContext);
    }

    /**
     * 卸载
     *
     * @param namespace   命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     */
    @Override
    public void uninstall(String namespace, String releaseName, String kubeContext) {
        HelmUtils.uninstall(namespace, releaseName, kubeContext);
    }
}
