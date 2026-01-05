package vip.gpg123.ai.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vip.gpg123.ai.domain.ExAcgRequest;
import vip.gpg123.ai.domain.ExAcgResponse;

@FeignClient(name = "exacg-api", url = "https://sd.exacg.cc/api/v1")
@Headers({"Content-Type: application/json", "Authorization: Bearer sk-FTyjCjmutsBTnbEQYIZFFqClDMbWTrlHPMVwDSgcLus"})
public interface ExAcgApi {

    @PostMapping("/generate_image")
    ExAcgResponse generateImage(@RequestBody ExAcgRequest request);

}
