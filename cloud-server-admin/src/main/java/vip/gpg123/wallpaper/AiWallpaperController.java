package vip.gpg123.wallpaper;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.domain.ExAcgRequest;
import vip.gpg123.ai.domain.ZImageRequest;
import vip.gpg123.ai.service.AliYunAiApi;
import vip.gpg123.ai.service.ExAcgApi;

@RestController
@RequestMapping("/wallpaper/ai")
public class AiWallpaperController {

    @Autowired
    private ExAcgApi exAcgApi;

    @Autowired
    private AliYunAiApi aliYunAiApi;

    /**
     * 生成
     * @param request r
     * @return r
     */
    @PostMapping("/generate_image")
    @ApiOperation(value = "生成")
    public Object generateImage(@RequestBody ExAcgRequest request) {
        return exAcgApi.generateImage(request);
    }

    /**
     * z-image
     * @param request r
     * @return r
     */
    @PostMapping("/z-image")
    @ApiOperation(value = "z-image生成")
    public Object zImage(@RequestBody ZImageRequest request){
        return aliYunAiApi.multimodalGeneration(request);
    }

}
