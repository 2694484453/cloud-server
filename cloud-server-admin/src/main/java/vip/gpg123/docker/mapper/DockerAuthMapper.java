package vip.gpg123.docker.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.docker.domain.DockerAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【docker_auth】的数据库操作Mapper
* @createDate 2025-12-12 22:58:29
* @Entity vip.gpg123.docker.domain.DockerAuth
*/
@Mapper
public interface DockerAuthMapper extends BaseMapper<DockerAuth> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    DockerAuth one(@Param("qw") DockerAuth dockerAuth);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<DockerAuth> list(@Param("qw") DockerAuth dockerAuth);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<DockerAuth> page(@Param("page") PageDomain page, @Param("qw") DockerAuth dockerAuth);
}




