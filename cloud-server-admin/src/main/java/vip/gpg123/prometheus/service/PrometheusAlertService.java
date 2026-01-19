package vip.gpg123.prometheus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gpg123.prometheus.dto.PrometheusAlertVO;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_alert(告警消息)】的数据库操作Service
* @createDate 2025-12-14 21:14:02
*/
public interface PrometheusAlertService extends IService<PrometheusAlert> {

    /**
     * list
     * @param alert a
     * @return r
     */
    List<PrometheusAlert> list(PrometheusAlert alert);

    /**
     * page
     * @param page p
     * @param alert a
     * @return r
     */
    IPage<PrometheusAlert> page(Page<PrometheusAlert> page, PrometheusAlert alert);

    /**
     * page
     * @param page p
     * @param alert a
     * @return r
     */
    IPage<PrometheusAlertVO> pageExtension(Page<PrometheusAlert> page, PrometheusAlert alert);
}
