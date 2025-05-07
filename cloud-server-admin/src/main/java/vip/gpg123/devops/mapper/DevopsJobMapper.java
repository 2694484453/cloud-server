package vip.gpg123.devops.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.devops.domain.DevopsJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【devops_job(普通任务)】的数据库操作Mapper
* @createDate 2025-05-08 01:57:47
* @Entity vip.gpg123.devops.domain.DevopsJob
*/
@Mapper
public interface DevopsJobMapper extends BaseMapper<DevopsJob> {

}




