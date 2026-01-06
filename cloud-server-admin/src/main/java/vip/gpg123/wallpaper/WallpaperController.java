package vip.gpg123.wallpaper;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.entity.SysDictData;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.DictUtils;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.domain.WallpaperSearchParams;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.StaticWallpaperService;
import vip.gpg123.wallpaper.service.WallpaperUploadService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallpaper")
public class WallpaperController {

    @Autowired
    private WallpaperUploadService wallpaperUploadService;

    @Autowired
    private WallPaperUploadController wallpaperUploadController;

    @Autowired
    private StaticWallpaperService staticWallpaperService;

    @Autowired
    private StaticWallpaperController staticWallpaperController;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DynamicWallpaperController dynamicWallpaperController;

    @Autowired
    private ISysNoticeService sysNoticeService;

    /**
     * 分类
     *
     * @return r
     */
    @GetMapping("/category")
    @ApiOperation(value = "cate")
    public AjaxResult cate() {
        List<SysDictData> list = DictUtils.getDictCache("wallpaper_category");
        return AjaxResult.success(list);
    }

    /**
     * over
     *
     * @return r
     */
    @GetMapping("/overView")
    public AjaxResult overView() {
        Map<String, Object> map = new HashMap<>();
        long total = staticWallpaperService.count();
        map.put("total", total);
        map.put("remain", stringRedisTemplate.opsForValue().get("exacg.remain"));
        map.put("generateCount", wallpaperUploadService.count());
        return AjaxResult.success(map);
    }

    /**
     * 总分页
     *
     * @param params p
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(WallpaperSearchParams params) {
        String cateName = ObjectUtil.defaultIfBlank(params.getCateName(), "");
        String name = ObjectUtil.defaultIfBlank(params.getName(), "");
        switch (cateName) {
            case "dynamic":
                DynamicWallpaper dynamicWallpaper = new DynamicWallpaper();
                dynamicWallpaper.setName(name);
                return dynamicWallpaperController.page(dynamicWallpaper);
            case "upload":
                WallpaperUpload wallpaperUpload = new WallpaperUpload();
                wallpaperUpload.setName(name);
                return wallpaperUploadController.page(wallpaperUpload);
            default:
                StaticWallpaper staticWallpaper = new StaticWallpaper();
                staticWallpaper.setName(name);
                staticWallpaper.setDirName(cateName);
                return staticWallpaperController.page(staticWallpaper);
        }
    }

    /**
     * 公告
     *
     * @return r
     */
    @GetMapping("/notice")
    public AjaxResult notice() {
        List<SysNotice> list = sysNoticeService.list(new LambdaQueryWrapper<SysNotice>()
                .eq(SysNotice::getNoticeType, "wallpaper")
        );
        return AjaxResult.success(list);
    }
}
