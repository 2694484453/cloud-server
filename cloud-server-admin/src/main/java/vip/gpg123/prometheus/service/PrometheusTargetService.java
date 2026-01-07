package vip.gpg123.prometheus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_exporter】的数据库操作Service
* @createDate 2025-11-19 01:40:53
*/
public interface PrometheusTargetService extends IService<PrometheusTarget> {

    /**
     * page
     * @param page p
     * @param prometheusTarget t
     * @return r
     */
    IPage<PrometheusTarget> page(Page<PrometheusTarget> page, PrometheusTarget prometheusTarget);

    /**
     * list
     * @param prometheusTarget t
     * @return r
     */
    List<PrometheusTarget> list(PrometheusTarget prometheusTarget);

}
