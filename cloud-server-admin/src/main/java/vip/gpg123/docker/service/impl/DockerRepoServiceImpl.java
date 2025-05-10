package vip.gpg123.docker.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.docker.domain.DockerRepo;
import vip.gpg123.docker.service.DockerRepoService;
import vip.gpg123.docker.mapper.DockerRepoMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【docker_repo(docker仓库认证信息表)】的数据库操作Service实现
* @createDate 2025-05-10 11:39:57
*/
@Service
public class DockerRepoServiceImpl extends ServiceImpl<DockerRepoMapper, DockerRepo> implements DockerRepoService{

}




