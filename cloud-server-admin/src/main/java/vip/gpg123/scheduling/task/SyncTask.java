package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vip.gpg123.app.HelmAppMarketController;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.nas.NasFrpClientController;
import vip.gpg123.prometheus.PrometheusTargetController;
import vip.gpg123.prometheus.PrometheusRuleController;
import vip.gpg123.prometheus.service.PrometheusApi;

@Component("syncTask")
@Slf4j
public class SyncTask {

    /**
     * 刷新frpc状态-无参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncNasFrpClientStatus() {
        SpringUtils.getBean(NasFrpClientController.class).sync();
    }

    /**
     * 执行prometheus状态同步
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncPrometheusExporterStatus() {
        SpringUtils.getBean(PrometheusTargetController.class).syncStatus();
    }

    /**
     * 执行prometheusRule状态同步
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncPrometheusRuleStatus() {
        SpringUtils.getBean(PrometheusRuleController.class).syncStatus();
    }

    /**
     * helmMarket
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncHelmAppMarket() {
        SpringUtils.getBean(HelmAppMarketController.class).sync();
    }

    /**
     * prometheusReload
     */
    @Transactional(rollbackFor = Exception.class)
    public void prometheusReload() {
        SpringUtils.getBean(PrometheusApi.class).reload();
    }

    /**
     * helm-repo
     */
    @Transactional(rollbackFor = Exception.class)
    public void helmRepoSync() {
        SpringUtils.getBean(HelmAppMarketController.class).sync();
    }

}
