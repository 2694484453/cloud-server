package vip.gpg123.vps.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.vps.domain.CloudHostServer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【cloud_host_server(云主机信息表)】的数据库操作Mapper
* @createDate 2025-05-26 23:15:54
* @Entity vip.gpg123.vps.domain.CloudHostServer
*/
@Mapper
public interface CloudHostServerMapper extends BaseMapper<CloudHostServer> {

}




