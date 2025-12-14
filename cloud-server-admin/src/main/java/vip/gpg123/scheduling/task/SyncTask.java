package vip.gpg123.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.gpg123.app.AppMarketController;
import vip.gpg123.common.utils.spring.SpringUtils;
import vip.gpg123.nas.NasFrpClientController;
import vip.gpg123.prometheus.PrometheusExporterController;
import vip.gpg123.prometheus.service.PrometheusApi;

import java.util.Map;

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
     * 刷新frpc状态-有参数
     */
    public void syncNasFrpClientStatus(Map<String,Object> map) {
        log.info("执行参数：{}", map.toString());
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
        SpringUtils.getBean(AppMarketController.class).sync();
    }

    /**
     * prometheusReload
     */
    public void prometheusReload() {
        SpringUtils.getBean(PrometheusApi.class).reload();
    }
}
