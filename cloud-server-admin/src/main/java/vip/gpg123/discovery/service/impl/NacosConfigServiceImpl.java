package vip.gpg123.discovery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.discovery.domain.NacosConfig;
import vip.gpg123.discovery.service.NacosConfigService;
import vip.gpg123.discovery.mapper.NacosConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【nacos_config(服务发现配置信息表)】的数据库操作Service实现
* @createDate 2025-05-26 21:47:37
*/
@Service
public class NacosConfigServiceImpl extends ServiceImpl<NacosConfigMapper, NacosConfig> implements NacosConfigService{

}




