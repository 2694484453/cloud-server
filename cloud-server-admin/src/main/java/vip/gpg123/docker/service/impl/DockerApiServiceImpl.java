package vip.gpg123.docker.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.gpg123.docker.domain.DockerRepo;
import vip.gpg123.docker.service.DockerApiService;
import vip.gpg123.docker.service.DockerRepoService;

@Service
public abstract class DockerApiServiceImpl implements DockerApiService {

    @Autowired
    private DockerRepoService dockerRepoService;

    /**
     * 登录
     *
     * @param dockerRepo 参数
     * @return r
     */
    @Override
    public boolean dockerLogin(DockerRepo dockerRepo) {
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://110.42.40.8:2375")
                .build();
        System.out.println(dockerClient.infoCmd().exec());
        return false;
    }
}
