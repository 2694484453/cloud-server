package vip.gpg123.scheduling.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.scheduling.domain.SysJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【sys_job_log(定时任务调度日志表)】的数据库操作Mapper
* @createDate 2025-05-28 01:25:41
* @Entity vip.gpg123.scheduling.domain.SysJobLog
*/
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {

}




