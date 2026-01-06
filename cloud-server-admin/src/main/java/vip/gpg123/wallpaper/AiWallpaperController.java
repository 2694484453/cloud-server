package vip.gpg123.wallpaper;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.domain.ExAcgRequest;
import vip.gpg123.ai.domain.ExAcgResponse;
import vip.gpg123.ai.domain.ZImageRequest;
import vip.gpg123.ai.service.AliYunAiApi;
import vip.gpg123.ai.service.ExAcgApi;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.WallpaperUploadService;

@RestController
@RequestMapping("/wallpaper/ai")
public class AiWallpaperController {

    @Autowired
    private ExAcgApi exAcgApi;

    @Autowired
    private AliYunAiApi aliYunAiApi;

    @Autowired
    private WallpaperUploadService wallpaperUploadService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成
     *
     * @param request r
     * @return r
     */
    @PostMapping("/generate_image")
    @ApiOperation(value = "生成")
    public AjaxResult generateImage(@RequestBody ExAcgRequest request) {
        //
        ExAcgResponse response = exAcgApi.generateImage(request);
        if (response != null) {
            String url = response.getData().getImage_url();
            WallpaperUpload wallpaperUpload = new WallpaperUpload();
            wallpaperUpload.setUrl(url);
            wallpaperUpload.setCreateTime(DateUtil.date());
            wallpaperUpload.setCreateBy("-1");
            wallpaperUpload.setName(url.substring(url.lastIndexOf('/') + 1));
            wallpaperUpload.setModelName(response.getData().getModel_name());
            stringRedisTemplate.opsForValue().set("exacg.remain", String.valueOf(response.getData().getRemaining_points()));
            wallpaperUploadService.save(wallpaperUpload);
            return AjaxResult.success("生成成功", wallpaperUpload);
        }
        return AjaxResult.error("生成失败");
    }

    /**
     * z-image
     *
     * @param request r
     * @return r
     */
    @PostMapping("/z-image")
    @ApiOperation(value = "z-image生成")
    public Object zImage(@RequestBody ZImageRequest request) {
        return aliYunAiApi.multimodalGeneration(request);
    }

}
