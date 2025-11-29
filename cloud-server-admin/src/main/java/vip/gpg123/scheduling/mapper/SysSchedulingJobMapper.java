package vip.gpg123.scheduling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.quartz.domain.SysJob;

import java.util.List;

@Mapper
public interface SysSchedulingJobMapper extends BaseMapper<SysJob> {


    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    SysJob one(@Param("qw") SysJob sysJob);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<SysJob> list(@Param("qw") SysJob sysJob);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<SysJob> page(@Param("page") PageDomain page, @Param("qw") SysJob sysJob);
}
