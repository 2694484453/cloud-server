package vip.gpg123.prometheus.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.prometheus.domain.PrometheusGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【prometheus_group】的数据库操作Mapper
* @createDate 2026-01-04 10:05:32
* @Entity vip.gpg123.prometheus.domain.PrometheusGroup
*/
@Mapper
public interface PrometheusGroupMapper extends BaseMapper<PrometheusGroup> {
    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    PrometheusGroup one(@Param("qw") PrometheusGroup PrometheusGroup);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<PrometheusGroup> list(@Param("qw") PrometheusGroup PrometheusGroup);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    IPage<PrometheusGroup> page(Page<PrometheusGroup> page, @Param("qw") PrometheusGroup PrometheusGroup);
}




