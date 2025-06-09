package vip.gpg123.repo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "helm-repo-api")
@Service
public interface HelmRepoApiService {

    /**
     * 获取helm仓库
     * @return r
     */
    @GetMapping("/index.yaml")
    String index(URI baseUri);

}
