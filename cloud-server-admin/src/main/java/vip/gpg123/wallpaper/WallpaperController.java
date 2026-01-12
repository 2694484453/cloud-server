package vip.gpg123.wallpaper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.constant.CacheConstants;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.domain.entity.SysDictData;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.core.redis.RedisCache;
import vip.gpg123.common.utils.DictUtils;
import vip.gpg123.common.utils.ip.IpUtils;
import vip.gpg123.system.domain.SysNotice;
import vip.gpg123.system.service.ISysNoticeService;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import vip.gpg123.wallpaper.domain.StaticWallpaper;
import vip.gpg123.wallpaper.domain.WallpaperKeyword;
import vip.gpg123.wallpaper.domain.WallpaperSearchParams;
import vip.gpg123.wallpaper.domain.WallpaperUpload;
import vip.gpg123.wallpaper.service.DynamicWallpaperService;
import vip.gpg123.wallpaper.service.StaticWallpaperService;
import vip.gpg123.wallpaper.service.WallpaperKeywordService;
import vip.gpg123.wallpaper.service.WallpaperUploadService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private RedisCache redisCache;

    @Autowired
    private DynamicWallpaperService dynamicWallpaperService;

    @Autowired
    private DynamicWallpaperController dynamicWallpaperController;

    @Autowired
    private ISysNoticeService sysNoticeService;

    @Autowired
    private WallpaperKeywordService wallpaperKeywordService;

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
     * 快速标签
     *
     * @return r
     */
    @GetMapping("/tags")
    @ApiOperation(value = "tags")
    public AjaxResult tags(@RequestParam("size") int size) {
        List<WallpaperKeyword> list;
        long count = wallpaperKeywordService.count();
        if (count <= size) {
            list = wallpaperKeywordService.list();
            return AjaxResult.success(list);
        } else {
            // 随机取
            list = wallpaperKeywordService.randomList(size);
        }
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
        map.put("total", staticWallpaperService.count());
        map.put("dynamicTotal", dynamicWallpaperService.count());
        map.put("remain", redisCache.getCacheSet(CacheConstants.AI_CONFIG_KEY + "exacg.remain"));
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
    public TableDataInfo page(Page<Object> page, WallpaperSearchParams params) {
        String cateName = ObjectUtil.defaultIfBlank(params.getCateName(), "");
        String name = ObjectUtil.defaultIfBlank(params.getName(), "");
        switch (cateName) {
            case "dynamic":
                DynamicWallpaper dynamicWallpaper = new DynamicWallpaper();
                dynamicWallpaper.setName(name);
                Page<DynamicWallpaper> dynamicWallpaperPage = new Page<>();
                BeanUtils.copyProperties(page, dynamicWallpaperPage);
                return dynamicWallpaperController.page(dynamicWallpaperPage, dynamicWallpaper);
            case "upload":
                WallpaperUpload wallpaperUpload = new WallpaperUpload();
                wallpaperUpload.setName(name);
                Page<WallpaperUpload> uploadWallpaperPage = new Page<>();
                BeanUtils.copyProperties(page, uploadWallpaperPage);
                return wallpaperUploadController.page(uploadWallpaperPage, wallpaperUpload);
            default:
                StaticWallpaper staticWallpaper = new StaticWallpaper();
                staticWallpaper.setName(name);
                staticWallpaper.setDirName(cateName);
                Page<StaticWallpaper> staticWallpaperPage = new Page<>();
                BeanUtils.copyProperties(page, staticWallpaperPage);
                return staticWallpaperController.page(staticWallpaperPage, staticWallpaper);
        }
    }

    /**
     * 剩余次数查询
     *
     * @param request r
     * @return r
     */
    @GetMapping("/remain")
    public AjaxResult timesRemain(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        Integer times = ObjectUtil.defaultIfNull(redisCache.getCacheObject(CacheConstants.AI_CONFIG_KEY + ip), 20);
        return AjaxResult.success(times);
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
