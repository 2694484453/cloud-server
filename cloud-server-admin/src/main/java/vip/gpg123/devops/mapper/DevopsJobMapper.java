package vip.gpg123.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.devops.domain.DevopsJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【devops_job(普通任务)】的数据库操作Mapper
* @createDate 2025-05-08 01:57:47
* @Entity vip.gpg123.devops.domain.DevopsJob
*/
@Mapper
public interface DevopsJobMapper extends BaseMapper<DevopsJob> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    DevopsJob one(@Param("qw") DevopsJob devopsJob);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<DevopsJob> list(@Param("qw") DevopsJob devopsJob);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<DevopsJob> page(@Param("page") PageDomain page, @Param("qw") DevopsJob devopsJob);

}




