package vip.gpg123.docker.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.gpg123.docker.domain.DockerAuth;
import vip.gpg123.docker.service.DockerAuthService;
import vip.gpg123.docker.mapper.DockerAuthMapper;
import org.springframework.stereotype.Service;

/**
* @author gaopuguang
* @description 针对表【docker_auth】的数据库操作Service实现
* @createDate 2025-12-12 22:58:29
*/
@Service
public class DockerAuthServiceImpl extends ServiceImpl<DockerAuthMapper, DockerAuth> implements DockerAuthService{

}




