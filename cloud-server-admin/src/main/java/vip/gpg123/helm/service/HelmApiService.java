package vip.gpg123.helm.service;


public interface HelmApiService {


    /**
     * 安装
     * @param namespace 命名空间
     * @param repoName 仓库名称
     * @param chartName chart名称
     * @param version 版本
     * @param kubeContext kubeContext
     */
    void install(String namespace, String repoName, String chartName, String version, String kubeContext);


    /**
     * 卸载
     * @param namespace 命名空间
     * @param releaseName 卸载名称
     * @param kubeContext HelmApp helmApp
     */
    void uninstall(String namespace, String releaseName, String kubeContext);
}
