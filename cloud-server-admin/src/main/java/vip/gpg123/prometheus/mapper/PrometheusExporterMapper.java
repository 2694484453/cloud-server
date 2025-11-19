package vip.gpg123.prometheus.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.prometheus.domain.PrometheusExporter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【prometheus_exporter】的数据库操作Mapper
* @createDate 2025-11-19 01:40:53
* @Entity vip.gpg123.prometheus.domain.PrometheusExporter
*/
@Mapper
public interface PrometheusExporterMapper extends BaseMapper<PrometheusExporter> {

}




