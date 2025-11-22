package vip.gpg123.nas.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vip.gpg123.framework.interceptor.impl.FrpServerBasicAuthRequestInterceptor;
import vip.gpg123.nas.domain.FrpServerHttpResponse;
import vip.gpg123.nas.domain.FrpServerInfo;

@FeignClient(name = "nas-frp-server", url = "${frp.server.url}", configuration = FrpServerBasicAuthRequestInterceptor.class)
@Service
public interface FrpServerApiService {

    /**
     * 查询服务信息
     * @return r
     */
    @GetMapping("/api/serverinfo")
    FrpServerInfo serverInfo();

    /**
     * 查询所有http代理
     * @return r
     */
    @GetMapping("/api/proxy/http")
    FrpServerHttpResponse httpList();

    /**
     * 查询所有https代理
     * @return r
     */
    @GetMapping("/api/proxy/https")
    FrpServerHttpResponse httpsList();

    /**
     * 查询所有tcp代理
     * @return r
     */
    @GetMapping("/api/proxy/tcp")
    FrpServerHttpResponse tcpList();

    /**
     * 查询所有udp代理
     * @return r
     */
    @GetMapping("/api/proxy/udp")
    FrpServerHttpResponse udpList();

    /**
     * 测试
     * @return r
     */
    @GetMapping("/api/proxy/http")
    String test();
}
