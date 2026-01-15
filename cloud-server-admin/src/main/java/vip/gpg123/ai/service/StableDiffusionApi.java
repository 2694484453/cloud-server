package vip.gpg123.ai.service;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sd-api", url = "http://192.168.3.18:7860")
public interface StableDiffusionApi {

    /**
     * 文生图
     * @param jsonObject json
     * @return r
     */
    @PostMapping("/sdapi/v1/txt2img")
    public JSONObject text2Image(@RequestBody JSONObject jsonObject);

    /**
     * 图生图
     * @param jsonObject json
     * @return r
     */
    @PostMapping("/sdapi/v1/img2img")
    public JSONObject img2Image(@RequestBody JSONObject jsonObject);

    /**
     * 查询配置
     * @return r
     */
    @GetMapping("/sdapi/v1/options")
    public JSONObject options();

    /**
     * 获取模型列表
     * @return r
     */
    @GetMapping("/sdapi/v1/sd-models")
    public JSONObject sdModels();

}
