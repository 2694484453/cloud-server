package vip.gpg123.wallpaper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.ai.domain.ExAcgRequest;
import vip.gpg123.ai.domain.ExAcgResponse;
import vip.gpg123.ai.domain.ZImageRequest;
import vip.gpg123.ai.service.AliYunAiApi;
import vip.gpg123.ai.service.ExAcgApi;
import vip.gpg123.common.constant.CacheConstants;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.redis.RedisCache;
import vip.gpg123.common.utils.ip.IpUtils;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.WallpaperUploadService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/wallpaper/ai")
public class WallpaperAiController {

    @Autowired
    private ExAcgApi exAcgApi;

    @Autowired
    private AliYunAiApi aliYunAiApi;

    @Autowired
    private WallpaperUploadService wallpaperUploadService;

    @Autowired
    private RedisCache redisCache;

    // 过期时间
    private static final long TODAY_END_TIMESTAMP =
            LocalDateTime.now(ZoneOffset.UTC)
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59)
                    .atZone(ZoneOffset.UTC)
                    .toInstant()
                    .getEpochSecond();

    private static final int initTimes = 20;

    /**
     * 生成
     *
     * @param request r
     * @return r
     */
    @PostMapping("/generate_image")
    @ApiOperation(value = "生成")
    public AjaxResult generateImage(@RequestBody ExAcgRequest request, HttpServletRequest httpServletRequest) {
        // 获取ip
        String ip = IpUtils.getIpAddr(httpServletRequest);
        if (StrUtil.isBlank(ip)) {
            return AjaxResult.error("无效ip");
        }
        // 忽略本地
        if (!"localhost".equals(ip)) {
            // 查询系统额度
            Integer systemTimes = redisCache.getCacheObject(CacheConstants.AI_CONFIG_KEY + "exacg.remain");
            if (systemTimes <= 1) {
                return AjaxResult.error("系统资源已耗尽");
            }
            String key = CacheConstants.AI_CONFIG_KEY + ip;
            // 查询是否存在key
            Boolean hasKey = redisCache.hasKey(key);
            if (hasKey) {
                // 查询今天剩余次数
                Integer times = redisCache.getCacheObject(key);
                if (times == 0) {
                    // 没有次数了
                    return AjaxResult.error("很抱歉您今天次数已使用完，想要使用更多请开通套餐！");
                }
            } else {
                // 否则生成
                redisCache.setCacheObject(CacheConstants.AI_CONFIG_KEY + ip, initTimes);
                // 设置过期时间为今天23:59:59
                redisCache.expire(key, TODAY_END_TIMESTAMP);
            }
        }

        ExAcgResponse response = exAcgApi.generateImage(request);
        if (response != null) {
            String url = response.getData().getImage_url();
            WallpaperUpload wallpaperUpload = new WallpaperUpload();
            wallpaperUpload.setUrl(url);
            wallpaperUpload.setCreateTime(DateUtil.date());
            wallpaperUpload.setCreateBy("-1");
            wallpaperUpload.setName(url.substring(url.lastIndexOf('/') + 1));
            wallpaperUpload.setModelName(response.getData().getModel_name());
            // 系统剩余额度
            redisCache.setCacheObject(CacheConstants.AI_CONFIG_KEY + "exacg.remain", String.valueOf(response.getData().getRemaining_points()));
            // 用户剩余额度
            Integer times = redisCache.getCacheObject(CacheConstants.AI_CONFIG_KEY + ip);
            if (times != null && times != 0) {
                times = times - 1;
                redisCache.setCacheObject(CacheConstants.AI_CONFIG_KEY + ip, times);
            }
            wallpaperUploadService.save(wallpaperUpload);
            return AjaxResult.success("生成成功，今天还剩余：" + times + "次", response.getData().getImage_url());
        }
        return AjaxResult.error("生成失败，请联系管理员");
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

    public Boolean checkTimes(String ip) {
        String key = CacheConstants.AI_CONFIG_KEY + ip;
        // 查询是否存在key
        Boolean hasKey = redisCache.hasKey(key);
        if (hasKey) {
            // 查询今天剩余次数
            Integer times = redisCache.getCacheObject(key);
            if (times == 0) {
                // 没有次数了
                return false;
            }
        } else {
            // 否则生成
            redisCache.setCacheObject(CacheConstants.AI_CONFIG_KEY + ip, initTimes);
            // 设置过期时间为今天23:59:59
            redisCache.expire(key, TODAY_END_TIMESTAMP);
            return true;
        }
        return false;
    }
}
