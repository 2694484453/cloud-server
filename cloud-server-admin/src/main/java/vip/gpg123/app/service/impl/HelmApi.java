package vip.gpg123.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.app.domain.HelmEntity;
import vip.gpg123.common.utils.K8sUtil;
import vip.gpg123.common.utils.helm.HelmUtils;
import vip.gpg123.kubernetes.domain.KubernetesCluster;
import vip.gpg123.kubernetes.mapper.KubernetesClusterMapper;

import java.io.File;

@Service
public class HelmApi {

    @Autowired
    private KubernetesClusterMapper kubernetesClusterMapper;

    /**
     * 安装
     *
     * @param entity e
     * @return r
     */
    public String install(HelmEntity entity) {
        KubernetesCluster kubernetesCluster = getKubernetesCluster(entity.getKubeContext());
        if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
            File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
            File valuesFile = K8sUtil.exportValuesToTempFile(entity.getChartValues());
            return HelmUtils.install(entity.getReleaseName(), entity.getNameSpace(), entity.getChartUrl(), valuesFile.getAbsolutePath(), entity.getKubeContext(), file.getAbsolutePath());
        } else {
            return "找不到集群：" + entity.getKubeContext();
        }
    }

    /**
     * uninstall
     *
     * @param entity e
     * @return r
     */
    public String uninstall(HelmEntity entity) {
        KubernetesCluster kubernetesCluster = getKubernetesCluster(entity.getKubeContext());
        if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
            File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
            return HelmUtils.uninstall(entity.getNameSpace(), entity.getReleaseName(), entity.getKubeContext(), file.getAbsolutePath());
        } else {
            return "找不到集群：" + entity.getKubeContext();
        }
    }

    /**
     * upgrade
     *
     * @param entity e
     * @return r
     */
    public String upgrade(HelmEntity entity) {
        KubernetesCluster kubernetesCluster = getKubernetesCluster(entity.getKubeContext());
        if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
            File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
            File valuesFile = K8sUtil.exportValuesToTempFile(entity.getChartValues());
            return HelmUtils.upgrade(entity.getReleaseName(), entity.getNameSpace(), entity.getChartUrl(), valuesFile.getAbsolutePath(), entity.getKubeContext(), file.getAbsolutePath());
        } else {
            return "找不到集群：" + entity.getKubeContext();
        }
    }

    /**
     * 列出应用
     *
     * @param entity e
     * @return r
     */
    public JSONArray listJsonArray(HelmEntity entity) {
        KubernetesCluster kubernetesCluster = getKubernetesCluster(entity.getKubeContext());
        if (ObjectUtil.isNotEmpty(kubernetesCluster)) {
            File file = K8sUtil.exportConfigToTempFile(kubernetesCluster.getConfig());
            return HelmUtils.listJsonArray(entity.getNameSpace(), entity.getKubeContext(), file.getAbsolutePath());
        } else {
            System.out.println("找不到集群：" + entity.getKubeContext());
            return null;
        }
    }


    /**
     * 获取参数
     *
     * @param entity e
     * @return r
     */
    public String values(HelmEntity entity) {
        return HelmUtils.showValues(entity.getChartUrl());
    }

    /**
     * 查看readme
     *
     * @param entity e
     * @return r
     */
    public String readme(HelmEntity entity) {
        return HelmUtils.showReadme(entity.getChartUrl());
    }

    public KubernetesCluster getKubernetesCluster(String kubeContext) {
        return kubernetesClusterMapper.selectOne(new LambdaQueryWrapper<KubernetesCluster>().eq(KubernetesCluster::getContextName, kubeContext));
    }
}
