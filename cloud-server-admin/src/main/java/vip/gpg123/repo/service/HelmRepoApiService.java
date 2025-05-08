package vip.gpg123.repo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.yaml.snakeyaml.Yaml;
import vip.gpg123.repo.domain.HelmRepoResponse;

@FeignClient(name = "helm-repo-api", url = "https://helm-repo.gpg123.vip")
@Service
public interface HelmRepoApiService {

    /**
     * 获取helm仓库
     * @return r
     */
    @GetMapping("/index.yaml")
    String index();

}
