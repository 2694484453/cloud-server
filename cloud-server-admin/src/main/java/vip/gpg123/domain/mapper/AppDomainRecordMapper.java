package vip.gpg123.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.domain.domain.AppDomainRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【app_domain(应用域名信息)】的数据库操作Mapper
* @createDate 2025-06-06 01:16:02
* @Entity vip.gpg123.domain.domain.AppDomain
*/
@Mapper
public interface AppDomainRecordMapper extends BaseMapper<AppDomainRecord> {

}




