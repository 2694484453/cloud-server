package vip.gpg123.discovery.service;

import org.springframework.stereotype.Service;
import vip.gpg123.discovery.domain.NacosNameSpace;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author gaopuguang
* @description 针对表【nacos_name_space(服务发现命名空间信息表)】的数据库操作Service
* @createDate 2025-05-24 23:56:39
*/
@Service
public interface NacosNameSpaceService extends IService<NacosNameSpace> {

}
