package vip.gpg123.vps.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import vip.gpg123.common.core.page.PageDomain;
import vip.gpg123.vps.domain.CloudHostServer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author gaopuguang
* @description 针对表【cloud_host_server(云主机信息表)】的数据库操作Mapper
* @createDate 2025-05-26 23:15:54
* @Entity vip.gpg123.vps.domain.CloudHostServer
*/
@Mapper
public interface CloudHostServerMapper extends BaseMapper<CloudHostServer> {

    /**
     * 根据 entity 条件，查询一条记录
     *
     */
    CloudHostServer selectOne(@Param("qw") CloudHostServer cloudHostServer);

    /**
     * 根据 entity 条件，查询全部记录
     *
     */
    List<CloudHostServer> selectList(@Param("qw") CloudHostServer cloudHostServer);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     */
    List<CloudHostServer> page(@Param("page") PageDomain page, @Param("qw") CloudHostServer cloudHostServer);
}




