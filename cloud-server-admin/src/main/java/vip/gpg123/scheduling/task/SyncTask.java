package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.gpg123.app.HelmAppMarketController;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.nas.NasFrpClientController;
import vip.gpg123.prometheus.PrometheusExporterController;
import vip.gpg123.prometheus.service.PrometheusApi;

@Component("syncTask")
@Slf4j
public class SyncTask {

    /**
     * 刷新frpc状态-无参数
     */
    public void syncNasFrpClientStatus() {
        SpringUtils.getBean(NasFrpClientController.class).sync();
    }

    /**
     * 执行prometheus状态同步
     */
    public void syncPrometheusExporterStatus() {
        SpringUtils.getBean(PrometheusExporterController.class).syncStatus();
    }

    /**
     * helmMarket
     */
    public void syncHelmAppMarket() {
        SpringUtils.getBean(HelmAppMarketController.class).sync();
    }

    /**
     * prometheusReload
     */
    public void prometheusReload() {
        SpringUtils.getBean(PrometheusApi.class).reload();
    }

    /**
     * helm-repo
     */
    public void helmRepoSync() {
        SpringUtils.getBean(HelmAppMarketController.class).sync();
    }
}
