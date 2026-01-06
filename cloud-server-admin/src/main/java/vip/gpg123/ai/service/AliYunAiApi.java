package vip.gpg123.ai.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vip.gpg123.ai.config.AliYunAiRequestConfig;
import vip.gpg123.ai.domain.ZImageRequest;

@FeignClient(name = "z-image-api",url = "${ali.ai.url}",configuration = AliYunAiRequestConfig.class)
public interface AliYunAiApi {

    /**
     * z-image图像生成
     * @param request r
     * @return r
     */
    @PostMapping("/multimodal-generation/generation")
    Object multimodalGeneration(@RequestBody ZImageRequest request);

}
