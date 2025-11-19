package vip.gpg123.app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.app.domain.IndexResponse;

@Service
@FeignClient(name = "helm-repo-api",url = "${repo.helm.url}")
public interface HelmRepoApi {

    /**
     * 查询index
     */
    @GetMapping("/index.yaml")
    IndexResponse index();
}
