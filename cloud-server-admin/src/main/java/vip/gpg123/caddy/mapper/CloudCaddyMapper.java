package vip.gpg123.caddy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.caddy.domain.CloudCaddy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.gpg123.common.core.page.PageDomain;

import java.util.List;

/**
 * @author gaopuguang
 * @description 针对表【cloud_caddy(caddy)】的数据库操作Mapper
 * @createDate 2025-12-02 23:06:47
 * @Entity vip.gpg123.caddy.domain.CloudCaddy
 */
@Mapper
public interface CloudCaddyMapper extends BaseMapper<CloudCaddy> {

    /**
     * 根据 entity 条件，查询一条记录
     */
    CloudCaddy one(@Param("qw") CloudCaddy cloudCaddy);

    /**
     * 根据 entity 条件，查询全部记录
     */
    List<CloudCaddy> list(@Param("qw") CloudCaddy cloudCaddy);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<CloudCaddy> page(@Param("page") PageDomain page, @Param("qw") CloudCaddy cloudCaddy);

}




