package vip.gpg123.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient(name = "helm-repo-api", url = "https://helm-repo.gpg123.vip")
public interface HelmRepoApi {

    /**
     * 查询index
     */
    @GetMapping(value = "/index.yaml")
    String index();
}
