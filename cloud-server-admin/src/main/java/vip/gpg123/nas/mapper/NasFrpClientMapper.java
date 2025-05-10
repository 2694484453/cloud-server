package vip.gpg123.nas.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.nas.domain.NasFrpClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【nas_frp_client(frp客户端配置信息表)】的数据库操作Mapper
* @createDate 2025-05-10 17:40:35
* @Entity vip.gpg123.nas.domain.NasFrpClient
*/
@Mapper
public interface NasFrpClientMapper extends BaseMapper<NasFrpClient> {

}




