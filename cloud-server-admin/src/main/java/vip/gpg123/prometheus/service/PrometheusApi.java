package vip.gpg123.prometheus.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.gpg123.prometheus.domain.PrometheusTargetResponse;

import java.net.URI;

@Service
@FeignClient(name = "prometheus-api")
public interface PrometheusApi {

    /**
     * 查询targets
     * @param baseUri url
     * @param state s
     * @return r
     */
    @GetMapping("/api/v1/targets")
    PrometheusTargetResponse targets(URI baseUri, @RequestParam(value = "state", required = false, defaultValue = "active") String state);

}
