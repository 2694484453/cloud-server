package vip.gpg123.nas.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.framework.interceptor.impl.FrpServerBasicAuthRequestInterceptor;
import vip.gpg123.git.domain.FrpServerHttpResponse;

@FeignClient(name = "nas-frp-server", url = "${frp.server.protocol}://${frp.server.host}:${frp.server.port}", configuration = FrpServerBasicAuthRequestInterceptor.class)
@Service
public interface FrpServerApiService {

    /**
     * 查询所有http代理
     * @return r
     */
    @GetMapping("/api/proxy/http")
    FrpServerHttpResponse httpList();
}
