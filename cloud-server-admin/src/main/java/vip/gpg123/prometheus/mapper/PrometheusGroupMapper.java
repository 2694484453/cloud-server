package vip.gpg123.prometheus.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.prometheus.domain.PrometheusGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【prometheus_group】的数据库操作Mapper
* @createDate 2026-01-04 10:05:32
* @Entity vip.gpg123.prometheus.domain.PrometheusGroup
*/
@Mapper
public interface PrometheusGroupMapper extends BaseMapper<PrometheusGroup> {

}




