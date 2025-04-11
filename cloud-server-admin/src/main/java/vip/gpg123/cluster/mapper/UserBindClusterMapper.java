package vip.gpg123.cluster.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.cluster.domain.UserBindCluster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author gaopuguang
* @description 针对表【user_bind_cluster】的数据库操作Mapper
* @createDate 2025-04-11 23:54:41
* @Entity vip.gpg123.cluster.domain.UserBindCluster
*/
@Mapper
public interface UserBindClusterMapper extends BaseMapper<UserBindCluster> {

}




