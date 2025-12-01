package vip.gpg123.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.domain.domain.CloudDomain;
import vip.gpg123.domain.service.CloudDomainService;
import vip.gpg123.domain.mapper.CloudDomainMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【app_domain(应用域名信息)】的数据库操作Service实现
* @createDate 2025-06-06 01:16:02
*/
@Service
public class DomainRecordServiceImpl extends ServiceImpl<CloudDomainMapper, CloudDomain> implements CloudDomainService {

}




