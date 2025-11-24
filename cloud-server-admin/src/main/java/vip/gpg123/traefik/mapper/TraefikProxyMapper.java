package vip.gpg123.traefik.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.traefik.domain.TraefikProxy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【traefik_proxy】的数据库操作Mapper
* @createDate 2025-11-25 00:42:19
* @Entity vip.gpg123.traefik.domain.TraefikProxy
*/

@Mapper
public interface TraefikProxyMapper extends BaseMapper<TraefikProxy> {

}




