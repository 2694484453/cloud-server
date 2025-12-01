package vip.gpg123.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.domain.domain.CloudDomain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【app_domain(应用域名信息)】的数据库操作Mapper
* @createDate 2025-06-06 01:16:02
* @Entity vip.gpg123.domain.domain.AppDomain
*/
@Mapper
public interface CloudDomainMapper extends BaseMapper<CloudDomain> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    CloudDomain one(@Param("qw") CloudDomain cloudDomain);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<CloudDomain> list(@Param("qw") CloudDomain cloudDomain);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<CloudDomain> page(@Param("page") PageDomain page, @Param("qw") CloudDomain cloudDomain);

}




