package vip.gpg123.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.devops.domain.DevopsCronJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【devops_cron_job(定时任务)】的数据库操作Mapper
* @createDate 2025-05-08 01:58:32
* @Entity vip.gpg123.devops.domain.DevopsCronJob
*/
@Mapper
public interface DevopsCronJobMapper extends BaseMapper<DevopsCronJob> {

}




