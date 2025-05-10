package vip.gpg123.docker.service;

import vip.gpg123.docker.domain.DockerRepo;

public interface DockerApiService {

    /**
     * 登录
     * @param dockerRepo 参数
     * @return r
     */
    boolean dockerLogin(DockerRepo dockerRepo);
}
