package vip.gpg123.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.devops.domain.DevopsCronJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.devops.domain.DevopsJob;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【devops_cron_job(定时任务)】的数据库操作Mapper
* @createDate 2025-05-08 01:58:32
* @Entity vip.gpg123.devops.domain.DevopsCronJob
*/
@Mapper
public interface DevopsCronJobMapper extends BaseMapper<DevopsCronJob> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    DevopsCronJob one(@Param("qw") DevopsCronJob devopsCronJob);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<DevopsCronJob> list(@Param("qw") DevopsCronJob devopsCronJob);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<DevopsCronJob> page(@Param("page") PageDomain page, @Param("qw") DevopsCronJob devopsCronJob);

}




