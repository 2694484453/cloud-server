package vip.gpg123.helm.service;


public interface HelmApiService {


    /**
     * 安装
     * @param namespace 命名空间
     * @param chartUrl url
     * @param kubeContext kubeContext
     */
    void install(String releaseName, String namespace, String chartUrl, String values, String kubeContext);


    /**
     * 卸载
     * @param namespace 命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     */
    void uninstall(String namespace, String releaseName, String kubeContext);
}
