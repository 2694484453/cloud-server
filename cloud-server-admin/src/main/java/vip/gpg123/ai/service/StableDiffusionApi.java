package vip.gpg123.ai.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sd-api", url = "http://192.168.3.18:7860")
public interface StableDiffusionApi {

    @PostMapping("/sdapi/v1/txt2img")
    public JSONObject text2Image(@RequestBody JSONObject jsonObject);


}
