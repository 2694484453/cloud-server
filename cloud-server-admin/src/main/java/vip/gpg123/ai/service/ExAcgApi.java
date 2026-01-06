package vip.gpg123.ai.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vip.gpg123.ai.config.ExAcgRequestConfig;
import vip.gpg123.ai.domain.ExAcgRequest;
import vip.gpg123.ai.domain.ExAcgResponse;

@FeignClient(name = "exacg-api", url = "https://sd.exacg.cc/api/v1", configuration = ExAcgRequestConfig.class)
public interface ExAcgApi {

    @PostMapping("/generate_image")
    ExAcgResponse generateImage(@RequestBody ExAcgRequest request);

}
