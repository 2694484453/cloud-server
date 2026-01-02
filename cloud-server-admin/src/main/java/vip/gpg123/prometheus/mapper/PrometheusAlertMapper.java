package vip.gpg123.prometheus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.prometheus.domain.PrometheusAlert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_alert(告警消息)】的数据库操作Mapper
* @createDate 2025-12-14 21:14:02
* @Entity vip.gpg123.prometheus.domain.PrometheusAlert
*/
@Mapper
public interface PrometheusAlertMapper extends BaseMapper<PrometheusAlert> {
    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    PrometheusAlert one(@Param("qw") PrometheusAlert prometheusAlert);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<PrometheusAlert> list(@Param("qw") PrometheusAlert prometheusAlert);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<PrometheusAlert> page(@Param("page") PageDomain page, @Param("qw") PrometheusAlert prometheusAlert);
}




