package vip.gpg123.prometheus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.prometheus.domain.PrometheusRule;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【prometheus_rule】的数据库操作Service
 * @createDate 2025-12-14 19:28:20
 */
public interface PrometheusRuleService extends IService<PrometheusRule> {

    /**
     * page
     * @param page page
     * @param prometheusRule r
     * @return r
     */
    IPage<PrometheusRule> page(Page<PrometheusRule> page, PrometheusRule prometheusRule);

    /**
     * list
     * @param prometheusRule r
     * @return r
     */
    List<PrometheusRule> list(PrometheusRule prometheusRule);
}
