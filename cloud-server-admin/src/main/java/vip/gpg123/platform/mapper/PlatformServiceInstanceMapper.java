package vip.gpg123.platform.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.platform.domain.PlatformServiceInstance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【platform_service_instance(平台服务实例配置信息表)】的数据库操作Mapper
* @createDate 2025-06-07 00:39:54
* @Entity vip.gpg123.platform.domain.PlatformServiceInstance
*/
@Mapper
public interface PlatformServiceInstanceMapper extends BaseMapper<PlatformServiceInstance> {

}




