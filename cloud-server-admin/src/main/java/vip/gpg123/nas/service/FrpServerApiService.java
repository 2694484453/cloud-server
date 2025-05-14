package vip.gpg123.nas.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.git.domain.FrpServerHttpResponse;

@FeignClient(name = "nas-frp-server", url = "${frp.server.protocol}://${frp.server.url}:${frp.server.port}")
@Service
public interface FrpServerApiService {

    /**
     * 查询所有http代理
     * @return r
     */
    @GetMapping("/api/proxy/http")
    FrpServerHttpResponse httpList();
}
