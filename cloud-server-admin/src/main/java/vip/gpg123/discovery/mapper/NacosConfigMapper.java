package vip.gpg123.discovery.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.discovery.domain.NacosConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【nacos_config(服务发现配置信息表)】的数据库操作Mapper
* @createDate 2025-05-26 21:47:37
* @Entity vip.gpg123.discovery.domain.NacosConfig
*/
@Mapper
public interface NacosConfigMapper extends BaseMapper<NacosConfig> {

}




