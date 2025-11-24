package vip.gpg123.traefik.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.traefik.domain.TraefikProxy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【traefik_proxy】的数据库操作Mapper
* @createDate 2025-11-25 00:42:19
* @Entity vip.gpg123.traefik.domain.TraefikProxy
*/

@Mapper
public interface TraefikProxyMapper extends BaseMapper<TraefikProxy> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    TraefikProxy one(@Param("qw") TraefikProxy traefikProxy);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<TraefikProxy> list(@Param("qw") TraefikProxy traefikProxy);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<TraefikProxy> page(@Param("page") PageDomain page, @Param("qw") TraefikProxy traefikProxy);

}




