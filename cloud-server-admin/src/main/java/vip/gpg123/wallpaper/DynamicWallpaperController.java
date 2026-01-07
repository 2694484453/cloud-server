package vip.gpg123.wallpaper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.gpg123.common.core.domain.AjaxResult;
import vip.gpg123.common.core.page.TableDataInfo;
import vip.gpg123.common.utils.PageUtils;
import vip.gpg123.wallpaper.domain.DynamicWallpaper;
import vip.gpg123.wallpaper.domain.DynamicWallpaperExtension;
import vip.gpg123.wallpaper.service.DynamicWallpaperService;

import java.util.List;

@RestController
@RequestMapping("/wallpaper/dynamic")
public class DynamicWallpaperController {

    @Autowired
    private DynamicWallpaperService dynamicWallpaperService;

    /**
     * list
     *
     * @param dynamicWallpaper dw
     * @return r
     */
    @GetMapping("/list")
    public AjaxResult list(DynamicWallpaper dynamicWallpaper) {
        List<DynamicWallpaper> dynamicWallpapers = dynamicWallpaperService.list(new LambdaQueryWrapper<DynamicWallpaper>()
                .like(StrUtil.isNotBlank(dynamicWallpaper.getName()), DynamicWallpaper::getName, dynamicWallpaper.getName())
        );
        return AjaxResult.success(dynamicWallpapers);
    }

    /**
     * page
     *
     * @param dynamicWallpaper dw
     * @return r
     */
    @GetMapping("/page")
    public TableDataInfo page(Page<DynamicWallpaper> page, DynamicWallpaper dynamicWallpaper) {
        // 查询
        IPage<DynamicWallpaperExtension> pageRes = dynamicWallpaperService.page(page, dynamicWallpaper);
        // 返回
        return PageUtils.toPageByIPage(pageRes);
    }
}
