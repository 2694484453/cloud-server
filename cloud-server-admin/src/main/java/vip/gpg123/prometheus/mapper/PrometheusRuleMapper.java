package vip.gpg123.prometheus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import vip.gpg123.prometheus.domain.PrometheusRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_rule】的数据库操作Mapper
* @createDate 2025-12-14 19:28:20
* @Entity vip.gpg123.prometheus.domain.PrometheusRule
*/
@Mapper
public interface PrometheusRuleMapper extends BaseMapper<PrometheusRule> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    PrometheusRule one(@Param("qw") PrometheusRule prometheusRule);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<PrometheusRule> list(@Param("qw") PrometheusRule prometheusRule);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<PrometheusRule> page(@Param("page") PageDomain page, @Param("qw") PrometheusRule  prometheusRule);
}




