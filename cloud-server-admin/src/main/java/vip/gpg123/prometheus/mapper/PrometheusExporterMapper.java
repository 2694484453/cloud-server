package vip.gpg123.prometheus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_exporter】的数据库操作Mapper
* @createDate 2025-11-19 01:40:53
* @Entity vip.gpg123.prometheus.domain.PrometheusExporter
*/
@Mapper
public interface PrometheusExporterMapper extends BaseMapper<PrometheusExporter> {
    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    PrometheusExporter one(@Param("qw") PrometheusExporter prometheusExporter);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<PrometheusExporter> list(@Param("qw") PrometheusExporter prometheusExporter);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<PrometheusExporter> page(@Param("page") PageDomain page, @Param("qw") PrometheusExporter prometheusExporter);
}




