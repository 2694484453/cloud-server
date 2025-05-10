package vip.gpg123.docker.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.gpg123.docker.domain.DockerRepo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【docker_repo(docker仓库认证信息表)】的数据库操作Mapper
* @createDate 2025-05-10 11:39:57
* @Entity vip.gpg123.docker.domain.DockerRepo
*/
@Mapper
public interface DockerRepoMapper extends BaseMapper<DockerRepo> {

}




