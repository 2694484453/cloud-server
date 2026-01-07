package vip.gpg123.prometheus.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.prometheus.domain.PrometheusTarget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_exporter】的数据库操作Mapper
* @createDate 2025-11-19 01:40:53
* @Entity vip.gpg123.prometheus.domain.PrometheusTarget
*/
@Mapper
public interface PrometheusTargetMapper extends BaseMapper<PrometheusTarget> {
    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    PrometheusTarget one(@Param("qw") PrometheusTarget prometheusTarget);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<PrometheusTarget> list(@Param("qw") PrometheusTarget prometheusTarget);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    IPage<PrometheusTarget> page(Page<PrometheusTarget> page, @Param("qw") PrometheusTarget prometheusTarget);
}




